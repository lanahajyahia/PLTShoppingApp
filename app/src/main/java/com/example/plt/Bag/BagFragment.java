package com.example.plt.Bag;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.content.DialogInterface;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plt.BottomNivBarActivity;
import com.example.plt.Common.Consts;
import com.example.plt.Shop.ShopFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plt.Common.Common;
import com.example.plt.Database.Database;
import com.example.plt.Models.Order;
import com.example.plt.Models.Request;
import com.example.plt.R;
import com.example.plt.ViewHolder.BagAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.example.plt.Notification.Notification.CHANNEL_1_ID;

import static android.app.Activity.RESULT_OK;


public class BagFragment extends Fragment {

    private NotificationManagerCompat notificationManager;

    private static final int PAYPAL_REQUEST_CODE = 9999;
    private RecyclerView bagRecyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private Button checkoutBtn, startShoppingBtn;
    private TextView txtTotalAmount, txtTotalPrice;

    private LinearLayout emptyScreen;

    private LinearLayout bottomBar;

    private RelativeLayout bagList;

    private boolean active = false;


    public Database localDB;

    private List<Order> bag = new ArrayList<>();
    private BagAdapter bagAdapter;

    FirebaseDatabase database;
    DatabaseReference requests;


    //paypal
    static PayPalConfiguration configuration = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Consts.PAYPAL_CLIENT_ID);
    String address, comment;
    JSONObject jsonObject;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bag, container, false);
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Request");

        notificationManager = NotificationManagerCompat.from(getContext());


        emptyScreen = view.findViewById(R.id.empty_bag_layout);
        bottomBar = view.findViewById(R.id.bottom_layout_bag);
        bagList = view.findViewById(R.id.list_bag_layout);

        bagRecyclerView = view.findViewById(R.id.bagRecyclerView);
        bagRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        bagRecyclerView.setLayoutManager(layoutManager);

        localDB = new Database(getContext());

        txtTotalPrice = view.findViewById(R.id.total_bag_cost);
        txtTotalAmount = view.findViewById(R.id.total_amount_bag);
        checkoutBtn = view.findViewById(R.id.btn_checkout);
        startShoppingBtn = view.findViewById(R.id.start_shop_btn);

        //init paypal
        Intent intent = new Intent(getContext(), PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        getContext().startService(intent);


        startShoppingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container
                        , new ShopFragment()).commit();
            }
        });

        loadBagList();

        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog_checkout();
            }
        });


        return view;
    }


    private void showAlertDialog_checkout() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("To complete your order you need to")
                .setMessage("enter your address: ");

        final LayoutInflater inflater = this.getLayoutInflater();
        View order_checkout_dialog = inflater.inflate(R.layout.order_checkout_dialog, null);

        alertDialog.setIcon(R.drawable.ic_security);
        alertDialog.setView(order_checkout_dialog);

        final EditText edtCheckout_address = order_checkout_dialog.findViewById(R.id.edtCheckout_address);
        final EditText edtCheckout_comment = order_checkout_dialog.findViewById(R.id.edtCheckout_comment);

        final RadioButton radioCOD = order_checkout_dialog.findViewById(R.id.radioCOD);
        final RadioButton radioPAYPAL = order_checkout_dialog.findViewById(R.id.radioPayPal);


        alertDialog.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //get address and comment
                address = edtCheckout_address.getText().toString();
                comment = edtCheckout_comment.getText().toString();

                if (!radioCOD.isChecked() && !radioPAYPAL.isChecked()) {

                    Toast.makeText(getContext(), "please select payment method", Toast.LENGTH_SHORT).show();

                } else if (radioPAYPAL.isChecked()) {

                    String formatAmount = txtTotalPrice.getText().toString()
                            .replace(Consts.DOLLAR, Consts.EMPTY)
                            .replace(Consts.PSEK, Consts.EMPTY);
//                float amount = Float.parseFloat(formatAmount);

                    PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(formatAmount),
                            Consts.USD,
                            Consts.APP_ORDER,
                            PayPalPayment.PAYMENT_INTENT_SALE);
                    Intent intent = new Intent(getContext(), PaymentActivity.class);
                    intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
                    intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
                    startActivityForResult(intent, PAYPAL_REQUEST_CODE);

                } else if (radioCOD.isChecked()) {
                    Request request = new Request(
                            Common.currentUser.getPhone(),
                            Common.currentUser.getFirstName() + " " + Common.currentUser.getLastName(),
                            address,
                            txtTotalPrice.getText().toString(),
                            "0",
                            comment,
                            bag,
                            Consts.UNPAID,
                            Consts.COD

                    );
                    //submit to firebase
                    //key cuurent.milli
                    requests.child(String.valueOf(System.currentTimeMillis()))
                            .setValue(request);
                    //delete cart
                    new Database(getActivity()).cleanBag(Common.currentUser.getPhone());
                    emptyScreen.setVisibility(View.VISIBLE);
                    bottomBar.setVisibility(View.INVISIBLE);
                    bagList.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext(), "orderPlaced", Toast.LENGTH_SHORT).show();
                    showNotificationOrderConfirmed();
                }

            }
        });


        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();

    }

    private void showNotificationOrderConfirmed() {

        Notification notification = new NotificationCompat.Builder(getContext(), CHANNEL_1_ID)
                .setSmallIcon(R.drawable.added_towishlist)
                .setContentTitle("Payment Confirmation")
                .setContentText("Thank you for you order! by the mean time your order is being processed:)")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Thank you for you order! by the mean time your order is being processed:)"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        notificationManager.notify(1, notification);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                        String paymentDetail = confirmation.toJSONObject().toString(4);
                        jsonObject = new JSONObject(paymentDetail);
                        createNewRequest();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        } else if (resultCode == Activity.RESULT_CANCELED)
            Toast.makeText(getContext(), "Payment canceled", Toast.LENGTH_SHORT).show();
        else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
            Toast.makeText(getContext(), "Invalid payment", Toast.LENGTH_SHORT).show();


    }

    private void createNewRequest() throws JSONException {
        Request request = new Request(
                Common.currentUser.getPhone(),
                Common.currentUser.getFirstName() + " " + Common.currentUser.getLastName(),
                address,
                txtTotalPrice.getText().toString(),
                "0",
                comment,
                bag,
                jsonObject.getJSONObject("response").getString("state"),
                Consts.PAYPAL

        );
        //submit to firebase
        //key cuurent.milli
        requests.child(String.valueOf(System.currentTimeMillis()))
                .setValue(request);
        //delete cart
        new Database(getActivity()).cleanBag(Common.currentUser.getPhone());
        emptyScreen.setVisibility(View.VISIBLE);
        bottomBar.setVisibility(View.INVISIBLE);
        bagList.setVisibility(View.INVISIBLE);
        Toast.makeText(getContext(), "orderPlaced", Toast.LENGTH_SHORT).show();
        showNotificationOrderConfirmed();

    }

    public void loadBagList() {
        bag = localDB.getBags(Common.currentUser.getPhone());
        Common.currentBagList = bag;
        if (bag.size() == 0 || bag == null) {
            emptyScreen.setVisibility(View.VISIBLE);
            bottomBar.setVisibility(View.GONE);
        } else {
            bagAdapter = new BagAdapter(this, bag);
            bagRecyclerView.setAdapter(bagAdapter);
            int totalPrice = 0, totalAmount = 0;
            for (Order order : bag) {
                totalAmount += (Integer.parseInt(order.getQuantity()));
                totalPrice += Integer.parseInt(order.getPrice()) * Integer.parseInt(order.getQuantity());
            }
            Locale locale = new Locale("en", "US");
            NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
            txtTotalPrice.setText(fmt.format(totalPrice));

            if (totalAmount == 1) {
                txtTotalAmount.setText(totalAmount + " item");
            } else {
                txtTotalAmount.setText(totalAmount + " items");
            }
        }

    }




    @Override
    public void onResume() {
        super.onResume();

    }


}

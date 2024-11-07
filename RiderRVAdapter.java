package com.example.unitconverter.RiderInterface;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unitconverter.R;

import java.util.ArrayList;

public class RiderRVAdapter extends RecyclerView.Adapter<RiderRVAdapter.UserViewHolder> {
    private ArrayList<RiderModalClass> userList;
    private Context context;
    private RiderRegisterDB db;
    private NotificationManager notifManager;

    public RiderRVAdapter(ArrayList<RiderModalClass> userList, Context context, NotificationManager notifManager) {
        this.userList = userList != null ? userList : new ArrayList<>();
        this.context = context;
        this.db = new RiderRegisterDB(context);
        this.notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Check for the existence of notification channels in Android 8.0+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("RiderChannel", "Rider Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            this.notifManager.createNotificationChannel(channel);
        }
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_rider, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        RiderModalClass user = userList.get(position);
        holder.nameText.setText(user.getName());
        holder.phoneText.setText(user.getPhone());
        holder.typeText.setText(user.getType());
        holder.idcardText.setText(user.getIdcard());
        holder.hoursText.setText(user.getHours());
        holder.daysText.setText(user.getDays());
        holder.cardText.setText(user.getCard());
        holder.emailText.setText(user.getEmail());
        holder.passText.setText(user.getPass());

        holder.accept.setOnClickListener(v -> {
            boolean isInserted = db.insertData(user.getName(), user.getPhone(), user.getType(), user.getIdcard(),
                    user.getHours(), user.getDays(), user.getCard(), user.getEmail(), user.getPass(),
                    user.getLatitude(), user.getLongitude(), user.getLocationName());

            String message = isInserted ? "Data saved successfully!" : "Error saving data!";
            String notificationMessage = isInserted ? user.getName() + " : Registered Successfully" : user.getName() + " : Application rejected your request";

            sendNotification("Registration Notification", notificationMessage);
            removeItem(position);
        });

        holder.reject.setOnClickListener(v -> {
            String notificationMessage = user.getName() + ": has been rejected.";
            sendNotification("Rejection Notification", notificationMessage);
            removeItem(position);
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    private void sendNotification(String title, String message) {
        Intent someIntent = new Intent(context, RiderRegister.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, someIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(context, "RiderChannel")
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.notification)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentIntent(pendingIntent)
                .setColor(Color.GREEN)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        notifManager.notify((int) System.currentTimeMillis(), notifBuilder.build());
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, phoneText, typeText, idcardText, hoursText, daysText, cardText, emailText, passText;
        Button accept, reject;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.name);
            phoneText = itemView.findViewById(R.id.phone);
            typeText = itemView.findViewById(R.id.type);
            idcardText = itemView.findViewById(R.id.idcard);
            hoursText = itemView.findViewById(R.id.hours);
            daysText = itemView.findViewById(R.id.days);
            cardText = itemView.findViewById(R.id.card);
            emailText = itemView.findViewById(R.id.email);
            passText = itemView.findViewById(R.id.pass);
            accept = itemView.findViewById(R.id.accept);
            reject = itemView.findViewById(R.id.reject);
        }
    }

    private void removeItem(int position) {
        userList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, userList.size());
    }
}

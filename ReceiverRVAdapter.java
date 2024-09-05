package com.example.unitconverter.ReceiverInterface;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unitconverter.R;

import java.util.ArrayList;

public class ReceiverRVAdapter extends RecyclerView.Adapter<ReceiverRVAdapter.UserViewHolder> {
    private final ArrayList<ReceiverModalClass> userList;
    private ReceiverRegisterDB db;
    private Context context;
    private NotificationManager notifManager;
    private final String channelID = "ReceiverChannel";
    private final String description = "Receiver Notification Channel";

    public ReceiverRVAdapter(ArrayList<ReceiverModalClass> userList, Context context, NotificationManager notifManager) {
        this.userList = (userList != null) ? userList : new ArrayList<>();
        this.context = context;
        this.db = new ReceiverRegisterDB(context);
        this.notifManager = notifManager;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notifChannel = new NotificationChannel(channelID, description, NotificationManager.IMPORTANCE_HIGH);
            notifChannel.enableLights(true);
            notifChannel.setLightColor(Color.GREEN);
            notifChannel.enableVibration(true);
            notifManager.createNotificationChannel(notifChannel);
        }
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_receiver, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        ReceiverModalClass user = userList.get(position);
        holder.referenceText.setText(user.getReference());
        holder.typeText.setText(user.getType());
        holder.memberText.setText(user.getMember());
        holder.requirementText.setText(user.getRequirement());
        holder.frequencyText.setText(user.getFrequency());
        holder.timeText.setText(user.getTime());
        holder.phoneText.setText(user.getPhone());
        holder.emailText.setText(user.getEmail());
        holder.passText.setText(user.getPass());

        holder.accept.setOnClickListener(v -> {
            boolean isInserted = db.insertData(user.getReference(), user.getType(), user.getMember(), user.getRequirement(), user.getFrequency(), user.getTime(), user.getPhone(), user.getEmail(), user.getPass());
            String message = isInserted ? "Data saved successfully!" : "Error saving data!";
            Toast.makeText(v.getContext(), message, Toast.LENGTH_SHORT).show();
            String notificationMessage = isInserted ? user.getReference() + " Registered Successfully" : user.getReference() + "Application rejected your request";
            sendNotification("Registration Notification", notificationMessage);
            removeItem(position);
        });

        holder.reject.setOnClickListener(v -> {
            String notificationMessage = user.getReference() + " has been rejected.";
            sendNotification("Rejection Notification", notificationMessage);
            removeItem(position);
        });
    }

    @Override
    public int getItemCount() {
        return (userList != null) ? userList.size() : 0;
    }

    private void sendNotification(String title, String message) {
        Intent intent = new Intent(context, ReceiverRVAdapter.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(context, channelID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.notification)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentIntent(pendingIntent);

        notifManager.notify((int) System.currentTimeMillis(), notifBuilder.build());
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView referenceText, typeText, memberText, requirementText, frequencyText, timeText, phoneText, emailText, passText;
        Button accept, reject;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            referenceText = itemView.findViewById(R.id.reference);
            typeText = itemView.findViewById(R.id.type);
            memberText = itemView.findViewById(R.id.member);
            requirementText = itemView.findViewById(R.id.requirement);
            frequencyText = itemView.findViewById(R.id.frequency);
            timeText = itemView.findViewById(R.id.time);
            phoneText = itemView.findViewById(R.id.phone);
            emailText = itemView.findViewById(R.id.email);
            passText = itemView.findViewById(R.id.pass);
            accept = itemView.findViewById(R.id.accept);
            reject = itemView.findViewById(R.id.reject);
        }
    }

    private void removeItem(int position) {
        ReceiverModalClass user = userList.get(position);
        userList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, userList.size());
    }
}

package com.example.feedhope.ReceiverInterface.ReceiverRegister;

import android.app.LauncherActivity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.feedhope.R;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ReceiverRVAdapter extends RecyclerView.Adapter<ReceiverRVAdapter.UserViewHolder> {
    private final ArrayList<ReceiverModalClass> userList;
    private ReceiverRegisterDB db;
    private Context context;
    private NotificationManager notifManager;

    public ReceiverRVAdapter(ArrayList<ReceiverModalClass> userList, Context context, NotificationManager notifManager) {
        this.userList = (userList != null) ? userList : new ArrayList<>();
        this.context = context;
        this.db = new ReceiverRegisterDB(context);
        this.notifManager = notifManager;

    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_register_receiver, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        ReceiverModalClass user = userList.get(position);
        holder.referenceText.setText(user.getReference());
        holder.typeText.setText(user.getType());
        holder.memberText.setText(String.valueOf(user.getMember()));
        holder.frequencyText.setText(user.getFrequency());
        holder.phoneText.setText(user.getPhone());
        holder.cardText.setText(String.valueOf(user.getCardNo()));
        holder.emailText.setText(user.getEmail());
        holder.passText.setText(user.getPass());
        holder.locationText.setText(user.getLocation());

        holder.accept.setOnClickListener(v -> {
            boolean isInserted = db.insertData(user.getReference(),user.getType(),user.getMember(),user.getFrequency(), user.getPhone(),user.getCardNo(), user.getEmail(), user.getPass(),user.getLocation());
            String message = isInserted ? "Data saved successfully!" : "Error saving data!";
            String notificationMessage = isInserted ? user.getReference() + " : Registered Successfully" : user.getReference() + " : Already Registered or any Null Field" ;
            sendNotification("Registration Notification", notificationMessage);
            removeItem(position);
        });

        holder.reject.setOnClickListener(v -> {
            String notificationMessage = user.getReference() + ": has been rejected.";
            sendNotification("Rejection Notification", notificationMessage);
            removeItem(position);
        });
    }


    @Override
    public int getItemCount() {
        return (userList != null) ? userList.size() : 0;
    }

    private void sendNotification(String title, String message) {
        Intent someIntent = new Intent(context, LauncherActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, someIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(context, "ReceiverChannel")
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.notification)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentIntent(pendingIntent);

        notifManager.notify((int) System.currentTimeMillis(), notifBuilder.build());
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView referenceText, typeText, memberText, frequencyText, phoneText,cardText, emailText, passText,locationText;
        Button accept, reject;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            referenceText = itemView.findViewById(R.id.reference);
            typeText = itemView.findViewById(R.id.type);
            memberText = itemView.findViewById(R.id.member);
            frequencyText = itemView.findViewById(R.id.frequency);
            phoneText = itemView.findViewById(R.id.phone);
            cardText = itemView.findViewById(R.id.cardNo);
            emailText = itemView.findViewById(R.id.email);
            passText = itemView.findViewById(R.id.pass);
            locationText = itemView.findViewById(R.id.location);

            accept = itemView.findViewById(R.id.accept);
            reject = itemView.findViewById(R.id.reject);
        }
    }
    private void removeItem(int position) {
        userList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, userList.size());

        // Remove item from SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("receiverPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Convert the updated list to JSON and save it back to SharedPreferences
        Gson gson = new Gson();
        String updatedJson = gson.toJson(userList);
        editor.putString("receiverList", updatedJson);
        editor.apply();
    }
}
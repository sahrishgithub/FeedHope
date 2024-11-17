package com.example.feedhope.RiderInterface.Register;

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

public class RiderRVAdapter extends RecyclerView.Adapter<RiderRVAdapter.UserViewHolder> {
    private ArrayList<RiderModalClass> userList;
    private Context context;
    private RiderRegisterDB db;
    private NotificationManager notifManager;

    public RiderRVAdapter(ArrayList<RiderModalClass> userList, Context context, NotificationManager notifManager) {
        this.userList = (userList != null) ? userList : new ArrayList<>();
        this.context = context;
        this.db = new RiderRegisterDB(context);
        this.notifManager = notifManager;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_register_rider, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        RiderModalClass user = userList.get(position);
        holder.nameText.setText(user.getName());
        holder.phoneText.setText(user.getPhone());
        holder.licenceText.setText(user.getLicence());
        holder.hoursText.setText(user.getHours());
        holder.daysText.setText(user.getDays());
        holder.cardText.setText(user.getCard());
        holder.emailText.setText(user.getEmail());
        holder.passText.setText(user.getPass());

        holder.accept.setOnClickListener(v -> {
            boolean isInserted = db.insertData(user.getName(),user.getPhone(),user.getLicence(),user.getHours(),user.getDays(), user.getCard(), user.getEmail(), user.getPass());
            String message = isInserted ? "Data saved successfully!" : "Error saving data!";
            String notificationMessage = isInserted ? user.getName() + " : Registered Successfully" : user.getName() + " : Application rejected your request" ;
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
        return (userList != null) ? userList.size() : 0;
    }

    private void sendNotification(String title, String message) {
        Intent someIntent = new Intent(context, LauncherActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, someIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(context, "RiderChannel")
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.notification)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentIntent(pendingIntent);

        notifManager.notify((int) System.currentTimeMillis(), notifBuilder.build());
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, phoneText, licenceText, hoursText, daysText, cardText,emailText, passText;
        Button accept, reject;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.name);
            phoneText = itemView.findViewById(R.id.phone);
            licenceText = itemView.findViewById(R.id.licence);
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

        // Remove item from SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("riderPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Convert the updated list to JSON and save it back to SharedPreferences
        Gson gson = new Gson();
        String updatedJson = gson.toJson(userList);
        editor.putString("riderList", updatedJson);
        editor.apply(); // Apply changes asynchronously
    }
}

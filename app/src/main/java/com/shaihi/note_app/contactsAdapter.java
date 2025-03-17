package com.shaihi.note_app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class contactsAdapter extends RecyclerView.Adapter<contactsAdapter.ViewHolder> {
    private List<Contact> contactsList;
    private Context context;
    private ContactDAO contactDAO;

    public contactsAdapter(List<Contact> contactsList, Context context) {
        this.contactsList = contactsList;
        this.context = context;
        this.contactDAO = ContactsDatabase.getInstance(context).contactDAO();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_item, parent, false);
        return new ViewHolder(view);
    }

    public void addContact(Contact contact) {
        contactsList.add(contact);
        notifyItemInserted(contactsList.size() - 1);
        Log.d("ContactApp", "Contact added: " + contact.toString());
    }

    public void removeContact(int position) {
        Contact contact = contactsList.get(position);
        // Delete from database
        contactDAO.delete(contact);
        // Remove from RecyclerView
        contactsList.remove(position);
        notifyItemRemoved(position);
        Log.d("ContactApp", "Contact removed: " + contact.toString());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Contact contact = contactsList.get(position);
        holder.firstName.setText(contact.getFirstName());
        holder.lastName.setText(contact.getLastName());
        holder.address.setText(contact.getAddress());
        holder.phoneNumber.setText(contact.getPhoneNumber());

        Log.d("ContactApp", "Binding contact: " + contact.toString());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                removeContact(holder.getAdapterPosition());
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView firstName;
        TextView lastName;
        TextView address;
        TextView phoneNumber;

        public ViewHolder(View itemView) {
            super(itemView);
            firstName = itemView.findViewById(R.id.tvFirstName);
            lastName = itemView.findViewById(R.id.tvLastName);
            address = itemView.findViewById(R.id.tvAddress);
            phoneNumber = itemView.findViewById(R.id.tvPhone);
        }
    }
}

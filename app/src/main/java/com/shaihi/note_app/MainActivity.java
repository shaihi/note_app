package com.shaihi.note_app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btnAdd;
    EditText etFirstName, etLastName, etAddress, etPhone;
    ListView listView;
    ContactDAO contactDAO;
    List<String> contactIds;
    ArrayAdapter<String> adapter;
    List<Contact> contactsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnAdd = findViewById(R.id.btnAdd);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etAddress = findViewById(R.id.etAddress);
        etPhone = findViewById(R.id.etPhone);
        listView = findViewById(R.id.lvContacts);

        ContactsDatabase db = ContactsDatabase.getInstance(this);
        contactDAO = db.contactDAO();
        contactsList = contactDAO.getAllContacts();
        // Extract IDs for display
        contactIds = new ArrayList<>();
        for (Contact c : contactsList) {
            contactIds.add(String.valueOf(c.getId()));
        }
        Log.d("ContactApp", "total contactsList: " + contactsList.size());
        // Adapter to show IDs
        adapter = new ArrayAdapter<>(
                this,
                R.layout.contact_item,
                contactIds
        );
        listView.setAdapter(adapter);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = etFirstName.getText().toString();
                String lastName = etLastName.getText().toString();
                String address = etAddress.getText().toString();
                String phoneNumber = etPhone.getText().toString();

                Contact contact = new Contact(firstName, lastName, address, phoneNumber);
                contactDAO.insert(contact);
                contact.setId(contactDAO.getLastContact().getId());
                Log.d("ContactApp", "adding Contact: " + contact.toString());
                contactsList.add(contact);
                contactIds.add(String.valueOf(contact.getId()));
                adapter.notifyDataSetChanged();
                etFirstName.setText("");
                etLastName.setText("");
                etAddress.setText("");
                etPhone.setText("");
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Show contact details
                Contact contact = contactsList.get(position);
                etFirstName.setText(contact.getFirstName());
                etLastName.setText(contact.getLastName());
                etAddress.setText(contact.getAddress());
                etPhone.setText(contact.getPhoneNumber());
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Remove from DB
                Contact toDelete = contactsList.get(position);
                contactDAO.delete(toDelete);
                Log.d("ContactApp", "Deleting contact: " + toDelete.toString());

                // Remove from both lists
                contactsList.remove(position);
                contactIds.remove(position);

                // Refresh UI
                adapter.notifyDataSetChanged();
                return true;
            }
        });
    }
}
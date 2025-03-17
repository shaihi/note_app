package com.shaihi.note_app;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btnAdd;
    EditText etFirstName, etLastName, etAddress, etPhone;
    RecyclerView rv;
    contactsAdapter adapter;
    ContactDAO contactDAO;

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
        rv = findViewById(R.id.rvContacts);
        rv.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
        ContactsDatabase db = ContactsDatabase.getInstance(this);
        contactDAO = db.contactDAO();
        List<Contact> contacts = contactDAO.getAllContacts();
        Log.d("ContactApp", "total contacts: " + contacts.size());
        adapter = new contactsAdapter(contactDAO.getAllContacts(), this);
        rv.setAdapter(adapter);
        btnAdd.setOnClickListener(v -> {
            String firstName = etFirstName.getText().toString();
            String lastName = etLastName.getText().toString();
            String address = etAddress.getText().toString();
            String phoneNumber = etPhone.getText().toString();

            Contact contact = new Contact(firstName, lastName, address, phoneNumber);
            Log.d("ContactApp", "adding Contact: " + contact.toString());
            contactDAO.insert(contact);
            adapter.addContact(contact);
            etFirstName.setText("");
            etLastName.setText("");
            etAddress.setText("");
            etPhone.setText("");
        });
    }
}
package com.example.ex3.activities;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ex3.App;
import com.example.ex3.R;
import com.example.ex3.adapters.ContactsListAdapter;
import com.example.ex3.entities.Contact;
import com.example.ex3.entities.ContactDetails;
import com.example.ex3.room.AppDB;
import com.example.ex3.room.ContactDao;
import com.example.ex3.viewmodels.ContactsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


public class ChatsSelector extends AppCompatActivity {

    private AppDB db;
    private ContactDao contactDao;
    private List<Contact> contacts;
    private ContactsListAdapter adapter;
    private RecyclerView listContactsView;
    private ContactsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats_selector);

        viewModel = new ViewModelProvider(this).get(ContactsViewModel.class);

        App.ACTIVE_CONTACT.observe(this, this::goToChat);

        if (getIntent().getExtras() != null) {
            viewModel.add((ContactDetails) getIntent().getExtras().get("contactToAdd"));
        }

        listContactsView = findViewById(R.id.listContacts);
        adapter = new ContactsListAdapter(this);
        listContactsView.setAdapter(adapter);
        listContactsView.setLayoutManager(new LinearLayoutManager(this));

        // when user refreshes, reload the viewModel
        SwipeRefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(() -> viewModel.reload());

        // whenever the list changes we will modify the adapter, and stop refreshing
        viewModel.get().observe(this, contacts -> {
            adapter.setContacts(contacts);
            refreshLayout.setRefreshing(false);
        });

        FloatingActionButton floatingActionButtonAdd = findViewById(R.id.floatingActionButtonAdd);
        floatingActionButtonAdd.setOnClickListener(view ->
            startActivity(new Intent(this, AddContactActivity.class))
        );
    }

    private void goToChat(Contact contact) {
        startActivity(new Intent(this, MessageListActivity.class)
                .putExtra("contact", contact)
        );
    }
}

// don't remove this comment
/*
public class ChatsSelector extends AppCompatActivity {

    private AppDB db;
    private ContactDao contactDao;
    private List<Contact> contacts;
    private ContactsListAdapter adapter;
    private RecyclerView listContacts;
    private ContactsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats_selector);

        ContactAPI contactAPI = new ContactAPI(null, null, "bob");
        contactAPI.get();

        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "LocalDB")
                .allowMainThreadQueries()
                .build();
        contactDao = db.contactDao();

        contacts = contactDao.index();

        adapter = new ContactsListAdapter(this);
        adapter.setContacts(contacts);

        listContacts = findViewById(R.id.listContacts);
        listContacts.setAdapter(adapter);
        listContacts.setLayoutManager(new LinearLayoutManager(this));
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();
        contacts.clear();
        contacts.addAll(contactDao.index());
        adapter.notifyDataSetChanged();
    }
}
 */
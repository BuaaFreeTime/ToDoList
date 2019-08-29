package comp5216.sydney.edu.au.todolist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Define variables
    ListView listView;
    ArrayList<ToDoItem> items;
    ArrayAdapter<ToDoItem> itemsAdapter;
    ToDoItemDB db;
    ToDoItemDao toDoItemDao;

    // Define request code
    public final int EDIT_ITEM_REQUEST_CODE = 647;
    public final int ADD_ITEM_REQUEST_CODE = 648;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use "activity_main.xml" as the layout
        setContentView(R.layout.activity_main);

        // Reference the "listView" variable to the id "lstView" in the layout
        listView = (ListView) findViewById(R.id.lstView);

        // Create an ArrayList of ToDoitem
        // Read from database
        db = ToDoItemDB.getDatabase(this.getApplication().getApplicationContext());
        toDoItemDao = db.toDoItemDao();
        readItemsFromDatabase();

        // Sort by date
        sortByDate();

        // Create an adapter for the list view using Android's built-in item layout
        itemsAdapter = new ArrayAdapter<ToDoItem>(this, android.R.layout.simple_list_item_1, items);

        // Connect the listView and the adapter
        listView.setAdapter(itemsAdapter);

        // Setup listView listeners
        setupListViewListener();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Deal whit the result from EditToDoItemActivity
        // Edit request
        if (requestCode == EDIT_ITEM_REQUEST_CODE) {
            // Actually change things
            if (resultCode == RESULT_OK) {
                // Extract item value from result extras
                String editedTitle = data.getStringExtra("title");
                String editedText = data.getStringExtra("text");
                int position = data.getIntExtra("position", -1);
                int editedId = data.getIntExtra("id", -1);
                ToDoItem editedItem = new ToDoItem(editedId, editedTitle, editedText, new Date());
                // Set the Adapter
                items.set(position, editedItem);
                // Update the database
                updateItemsToDatabase(editedItem);
                // Send log and message
                Log.i("Updated Item in list:", editedTitle + ",position:" + position);
                Toast.makeText(this, "updated:" + editedTitle, Toast.LENGTH_SHORT).show();
                // Sort by date
                sortByDate();
                // Notify the change
                itemsAdapter.notifyDataSetChanged();
            }
            // If the result == RESULT_CANCELED
            //  Nothing need to do
        }
        // Add item request
        if (requestCode == ADD_ITEM_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Extract name value from result extras
                String editedTitle = data.getStringExtra("title");
                String editedText = data.getStringExtra("text");
                ToDoItem newItem = new ToDoItem(editedTitle, editedText, new Date());
                // // Set the Adapter
                items.add(newItem);
                // Add item to the database
                addItemsToDatabase(newItem);
                // Send log and message
                Log.i("Add Item in list:", "Added item title : " + editedTitle);
                Toast.makeText(this, "Added:" + editedTitle, Toast.LENGTH_SHORT).show();
                sortByDate();
                itemsAdapter.notifyDataSetChanged();
            }
            // If the result == RESULT_CANCELED
            //  Nothing need to do
        }
    }

    public void onAddItemClick(View view) {
        // Used for handle the click Edit/Add Button event

        // Define variables
        String updateTitle = "";
        String updateText = "";

        Log.i("MainActivity", "Clicked Add Button ");

        Intent intent = new Intent(MainActivity.this, EditToDoItemActivity.class);

        if (intent != null) {
            // put "extras" into the bundle for access in the edit activity
            intent.putExtra("title", updateTitle);
            intent.putExtra("text", updateText);
            // brings up the second activity
            startActivityForResult(intent, ADD_ITEM_REQUEST_CODE);
            // Sort by date
            sortByDate();
            itemsAdapter.notifyDataSetChanged();
        }
    }

    private void setupListViewListener() {
        // Used for handle the click item in listvied event

        // Long click to delete item
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long rowId) {
                Log.i("MainActivity", "Long Clicked item " + position);
                // Setting a dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.dialog_delete_title)
                        .setMessage(R.string.dialog_delete_msg)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // Remove item form database
                                        deleteItems(items.get(position));
                                        // Remove item from the ArrayList
                                        items.remove(position);
                                        // Notify listView adapte to update the list
                                        itemsAdapter.notifyDataSetChanged();
                                    }
                                })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // User cancelled the dialog
                                        // Nothing happens
                                    }
                                });
                builder.create().show();
                return true;
            }
        });

        // Short click to edit item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the original title and text and id
                String updateTitle = (String) itemsAdapter.getItem(position).getTitle();
                String updateText = (String) itemsAdapter.getItem(position).getText();
                int updateId = (int) itemsAdapter.getItem(position).getId();

                Log.i("MainActivity", "Clicked item " + position + ": " + updateTitle);

                Intent intent = new Intent(MainActivity.this, EditToDoItemActivity.class);

                if (intent != null) {
                    // put "extras" into the bundle for access in the edit activity
                    intent.putExtra("title", updateTitle);
                    intent.putExtra("text", updateText);
                    intent.putExtra("position", position);
                    intent.putExtra("id", updateId);
                    // Brings up the second activity
                    startActivityForResult(intent, EDIT_ITEM_REQUEST_CODE);
                    // Sort by date
                    sortByDate();
                    itemsAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void sortByDate() {
        // A method to sort items by date
        Collections.sort(items, new Comparator<ToDoItem>() {

            @Override
            public int compare(ToDoItem o1, ToDoItem o2) {
                boolean flag = o1.getDate().before(o2.getDate());
                if (flag) return 1;
                    else return -1;
            }
        });
    }

    private void readItemsFromDatabase() {
        //Use asynchronous task to run query on the background and wait for result
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    //read items from database
                    List<ToDoItemDF> itemsFromDB = toDoItemDao.listAll();
                    items = new ArrayList<ToDoItem>();
                    if (itemsFromDB != null & itemsFromDB.size() > 0) {
                        for (ToDoItemDF item : itemsFromDB) {
                            items.add(new ToDoItem(item.getToDoItemID(), item.getToDoItemTitle(), item.getToDoItemText(), item.getToDoItemDate()));
                            Log.i("SQLite read item", "ID: " + item.getToDoItemID() + " Title: " + item.getToDoItemTitle());
                        } }
                    return null; }
            }.execute().get();
        }
        catch(Exception ex) {
            Log.e("readItemsFromDatabase", ex.getStackTrace().toString());
        }
    }


    private void addItemsToDatabase(ToDoItem itemInput) {
        // A method to add item to database
        final ToDoItem item;
        item = itemInput;
        //Use asynchronous task to run query on the background to avoid locking UI
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                ToDoItemDF toDoItemDF = new ToDoItemDF(item);
                // Insert a new item to database
                toDoItemDao.insert(toDoItemDF);
                Log.i("SQLite saved item", toDoItemDF.getToDoItemTitle());
                return null;
            }
        }.execute();
    }

    private void updateItemsToDatabase(ToDoItem itemInput) {
        // A method to update the item
        final ToDoItem item;
        item = itemInput;
        //Use asynchronous task to run query on the background to avoid locking UI
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                ToDoItemDF toDoItemDF = new ToDoItemDF(item);
                toDoItemDF.setToDoItemID(item.getId());
                // Update item
                toDoItemDao.update(toDoItemDF);
                Log.i("SQLite update item", toDoItemDF.getToDoItemTitle());
                return null;
            }
        }.execute();
    }

    private void deleteItems(ToDoItem itemInput) {
        // A method to delete items
        final ToDoItem item;
        item = itemInput;
        //Use asynchronous task to run query on the background to avoid locking UI
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                ToDoItemDF toDoItemDF = new ToDoItemDF(item);
                toDoItemDF.setToDoItemID(item.getId());
                // Delete item
                toDoItemDao.delete(toDoItemDF);
                Log.i("SQLite delete item", toDoItemDF.getToDoItemTitle());
                return null;
            }
        }.execute();
    }
}

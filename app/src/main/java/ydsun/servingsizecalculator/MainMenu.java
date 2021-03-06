package ydsun.servingsizecalculator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

// ArrayAdapter
// List view: {views: pots.xml}

public class MainMenu extends AppCompatActivity {
    public static final String POT_LIST = "POT LIST";
    public static final String SELECTED_POS = "Selected Position For Edit";
    public static final int REQUEST_CODE = 114;
    public static final int REQUEST_EDIT = 514;
    public static final int REQUEST_REMOVE = 1919;
    private PotCollection my_pot_collection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        my_pot_collection = new PotCollection(getApplicationContext());
        my_pot_collection.load();
        display_list_view();

        setupAddPotButton();
        RegisterClickCallBackForPots();
        RegisterLongClickForEditing();
        setupRemovePotButton();
//        Intent intent = getIntent();
//        int pot_weight = intent.getIntExtra("Pot Weight", 0);
//        String pot_name = intent.getStringExtra("Pot Name");
//
//        Pot new_pot = new Pot(pot_name, pot_weight);
//
//        my_pot_collection.addPot(new_pot);

//        String[] pot_description = my_pot_collection.getPotDescriptions();
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.pots, pot_description);
//        ListView list = (ListView) findViewById(R.id.Pots);
//        list.setAdapter(adapter);
    }

    // Return for result from other activities
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case REQUEST_CODE:
                if(resultCode == Activity.RESULT_OK){
                    String pot_name = data.getStringExtra("Pot Name");
                    int pot_weight = data.getIntExtra("Pot Weight", 0);
                    Pot new_pot = new Pot(pot_name, pot_weight);
                    my_pot_collection.addPot(new_pot);
                    my_pot_collection.save();
                    display_list_view();
                }
                else{
                    display_list_view();
                }
        }
        switch(requestCode){
            case REQUEST_EDIT:
                if(resultCode == Activity.RESULT_OK){
                    String pot_name_new = data.getStringExtra("Pot Name");
                    int pot_weight_new = data.getIntExtra("Pot Weight", 0);
                    int position = data.getIntExtra("Position", 0);
                    Pot edited_pot = new Pot(pot_name_new, pot_weight_new);
                    my_pot_collection.changePot(edited_pot,position);
                    my_pot_collection.save();
                    display_list_view();
                }
                else{
                    my_pot_collection.save();
                    display_list_view();
                }
        }

        switch(requestCode){
            case REQUEST_REMOVE:
                if(resultCode == Activity.RESULT_OK){
                    int position = data.getIntExtra("removing_pos",0);
                    Toast.makeText(getApplicationContext(), "Removed Pot: " + my_pot_collection.getPot(position).getName(), Toast.LENGTH_LONG).show();
                    my_pot_collection.removePot(position);
                    my_pot_collection.clear_data();
                    my_pot_collection.save();
                    display_list_view();
                }
        }
    }

    // method to setup list view for pots
    private void display_list_view() {
        String[] pot_description = my_pot_collection.getPotDescriptions();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.pots, pot_description);
        ListView list = (ListView) findViewById(R.id.Pots);
        list.setAdapter(adapter);
    }

    // initiate Add Pot Button
    private void setupAddPotButton(){
        Button Add_Pot = (Button) findViewById(R.id.add_pot);
        Add_Pot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AddingNewPot.makeIntentForAdding(MainMenu.this);
                //startActivity(intent);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    // initiate Click on Pot Calculation Menu Intent
    private void RegisterClickCallBackForPots(){
        ListView list = (ListView) findViewById(R.id.Pots);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                Intent intent = Calculations.makeIntentForCalc(MainMenu.this, my_pot_collection.getPot(position));
//                Pot selected_pot = my_pot_collection.getPot(position);
//                String selected_pot_name = selected_pot.getName();
//                int selected_pot_weight = selected_pot.getWeightInG();
//                intent.putExtra("Selected Name", selected_pot_name);
//                intent.putExtra("Selected Weight", selected_pot_weight);
                startActivity(intent);
            }
        });
    }

    // initiate Pot Editing feature
    private void RegisterLongClickForEditing(){
        ListView long_list = (ListView) findViewById(R.id.Pots);
        long_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                Intent intent = AddingNewPot.makeIntentForEditing(MainMenu.this, position);
//              intent.putExtra("Selected Position For Edit", position);
                startActivityForResult(intent, REQUEST_EDIT);
                return true;
            }
        });
    }

    // initiate Pot Removing feature
    private void setupRemovePotButton(){
        Button Remove_Pot = (Button) findViewById(R.id.remove_button);
        Remove_Pot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] pot_description = my_pot_collection.getPotDescriptions();
                Intent intent = RemovePot.makeIntentForRemoving(MainMenu.this, pot_description);
//              intent.putParcelableArrayListExtra("Pot Collection", my_pot_collection);
//                intent.putExtra("Pot Collection", pot_description);
                startActivityForResult(intent, REQUEST_REMOVE);
            }
        });
    }
}

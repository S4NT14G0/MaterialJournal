package com.developer.android.sroig.materialjournal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.developer.android.sroig.materialjournal.models.Database;
import com.developer.android.sroig.materialjournal.models.JournalItem;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * Created by Santiago Roig on 11/3/15.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //Drop the table for testing
        //Database.getInstance(this).dropTable();

        // Uncomment to get rid of everything in DB for testing
        //Database.getInstance(this).deleteAll();

        // Loads and adds in our table
        Database.getInstance(this).addTable();

        // Load all of the cards into our  main view
        loadCardsList();
    }

    public void loadCardsList() {
        // Floating action button from https://github.com/makovkastar/FloatingActionButton
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        ArrayList<Card> cards = new ArrayList<Card>();

        // Go through each item in our DB
        for (int i = 1; i <= Database.getInstance(this).rowCount(); i++) {

            // Get the first row from our DB
            JournalItem item = Database.getInstance(this).getRow(i);

            // Check if our item is null in case the user deleted this card
            if (item != null) {
                // Cards from https://github.com/gabrielemariotti/cardslib
                Card card = new Card (this);

                // Create a header for our card
                CardHeader header = new CardHeader(this);

                // Set up all of the elements on our card
                header.setTitle(item.getTitle());
                card.setTitle(Database.getInstance(this).dateToString(item.getDate()));
                card.addCardHeader(header);
                CardThumbnail thumb = new CardThumbnail(this);
                thumb.setDrawableResource(R.drawable.ic_description_black_36dp);
                card.addCardThumbnail(thumb);
                card.setId(item.getId() + "");

                //Set onClick listener
                card.setOnClickListener(new Card.OnCardClickListener() {
                    @Override
                    public void onClick(Card card, View view) {
                        Intent intent = new Intent(MainActivity.this, JournalEditActivity.class);
                        intent.putExtra("itemId", card.getId());
                        startActivity(intent);
                    }
                });

                // Add card to our list of cards
                cards.add(card);
            }
        }

        // Create a card adapter and add our list of cards to our Card list view
        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(this, cards);
        CardListView listView = (CardListView) this.findViewById(R.id.myList);

        // Attaches floating action button to listview
        fab.attachToListView(listView);

        if (listView != null) {
            listView.setAdapter(mCardArrayAdapter);
        }
    }

    public void createNewJournalItem(View view) {

        JournalItem item = new JournalItem();
        item.setTitle("");
        item.setText("");
        item.setDate(Calendar.getInstance());
        item.setLocation("");
        item.setTags("");
        //item.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.ic_stars_black_18dp));

        long id = Database.getInstance(this).addRow(item);

        Intent intent = new Intent(MainActivity.this, JournalEditActivity.class);
        intent.putExtra("itemId", "" + id);
        startActivity(intent);

    }

    public void openSearch(View view) {
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(intent);
    }

}

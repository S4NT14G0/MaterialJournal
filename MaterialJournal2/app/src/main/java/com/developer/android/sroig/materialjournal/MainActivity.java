package com.developer.android.sroig.materialjournal;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
 * Created by roigs23 on 11/3/15.
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Database.getInstance(this).addTable();

        // Uncomment to get rid of everything in DB
        //Database.getInstance(this).deleteAll();

        loadCardsList();
    }

    public void loadCardsList() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        ArrayList<Card> cards = new ArrayList<Card>();

        for (int i = 1; i < Database.getInstance(this).rowCount(); i++) {

            JournalItem item = Database.getInstance(this).getRow(i);

            Card card = new Card (this);

            CardHeader header = new CardHeader(this);

            header.setTitle(item.getTitle());
            card.setTitle(Database.getInstance(this).dateToString(item.getDate()));
            card.addCardHeader(header);
            CardThumbnail thumb = new CardThumbnail(this);
            thumb.setDrawableResource(R.drawable.ic_stars_black_18dp);
            card.addCardThumbnail(thumb);

            cards.add(card);

        }

//        for (int i = 0; i<20; i++) {
//            // Create a Card
//            Card card = new Card(this);
//            // Create a CardHeader
//            CardHeader header = new CardHeader(this);
//            // Add Header to card
//            header.setTitle("Journal Entry: " + i);
//            card.setTitle("Date");
//            card.addCardHeader(header);
//            CardThumbnail thumb = new CardThumbnail(this);
//            thumb.setDrawableResource(R.drawable.ic_stars_black_18dp);
//            card.addCardThumbnail(thumb);
//
//            cards.add(card);
//        }

        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(this, cards);
        CardListView listView = (CardListView) this.findViewById(R.id.myList);

        fab.attachToListView(listView);

        if (listView != null) {
            listView.setAdapter(mCardArrayAdapter);
        }
    }

    public void createNewJournalItem(View view) {

        JournalItem item = new JournalItem();
        item.setText("Hello");
        item.setDate(Calendar.getInstance());
        item.setTitle("Title bruh");
        item.setLocation("Pike house");
        item.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.ic_stars_black_18dp));

         Database.getInstance(this).addRow(item);

        Toast.makeText(this, "Test: " + Database.getInstance(this).rowCount(), Toast.LENGTH_SHORT).show();

        loadCardsList();
    }


}

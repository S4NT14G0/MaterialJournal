package com.developer.android.sroig.materialjournal;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.developer.android.sroig.materialjournal.models.Database;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * Created by roigs23 on 11/3/15.
 */
public class MainActivity extends Activity {

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ArrayList<Card> cards = new ArrayList<Card>();

        for (int i = 0; i<20; i++) {
            // Create a Card
            Card card = new Card(this);
            // Create a CardHeader
            CardHeader header = new CardHeader(this);
            // Add Header to card
            header.setTitle("Journal Entry: " + i);
            card.setTitle("Date");
            card.addCardHeader(header);
            CardThumbnail thumb = new CardThumbnail(this);
            thumb.setDrawableResource(R.drawable.ic_stars_black_18dp);
            card.addCardThumbnail(thumb);

            cards.add(card);
        }

        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(this, cards);
        CardListView listView = (CardListView) this.findViewById(R.id.myList);

        fab.attachToListView(listView);

        if (listView != null) {
            listView.setAdapter(mCardArrayAdapter);
        }
    }

    public void populateCardsList() {


    }


}

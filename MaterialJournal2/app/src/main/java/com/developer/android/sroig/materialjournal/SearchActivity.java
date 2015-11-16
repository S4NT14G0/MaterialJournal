package com.developer.android.sroig.materialjournal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.developer.android.sroig.materialjournal.models.Database;
import com.developer.android.sroig.materialjournal.models.JournalItem;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * Created by Santiago Roig on 11/15/15.
 */
public class SearchActivity extends AppCompatActivity {

    MaterialEditText editTextTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        editTextTag = (MaterialEditText) findViewById(R.id.textSearch);
        wrapSearch();
    }

    public void searchForJournalItem(View view) {
        // Hide keyboard when you click search
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        wrapSearch();
    }

    public void wrapSearch() {

        if (!editTextTag.getText().toString().isEmpty()) {
            ArrayList<Card> cards = new ArrayList<Card>();

            ArrayList<JournalItem> items = Database.getInstance(this).findItemsByTag(editTextTag.getText().toString());

            for (JournalItem item : items) {
                Card card = new Card(this);

                // Create a header for our card
                CardHeader header = new CardHeader(this);

                // Set up all of the elements on our card
                header.setTitle(item.getTitle());
                card.setTitle(Database.getInstance(this).dateToString(item.getDate()));
                card.addCardHeader(header);
                CardThumbnail thumb = new CardThumbnail(this);
                thumb.setDrawableResource(R.drawable.ic_stars_black_18dp);
                card.addCardThumbnail(thumb);
                card.setId(item.getId() + "");

                //Set onClick listener
                card.setOnClickListener(new Card.OnCardClickListener() {
                    @Override
                    public void onClick(Card card, View view) {
                        Intent intent = new Intent(SearchActivity.this, JournalEditActivity.class);
                        intent.putExtra("itemId", card.getId());
                        startActivity(intent);
                    }
                });

                // Add card to our list of cards
                cards.add(card);
            }

            // Create a card adapter and add our list of cards to our Card list view
            CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(this, cards);
            CardListView listView = (CardListView) this.findViewById(R.id.mySearchList);

            if (listView != null) {
                listView.setAdapter(mCardArrayAdapter);
            }
        }

    }
}

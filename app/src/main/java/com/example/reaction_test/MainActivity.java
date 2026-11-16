package com.example.reaction_test;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button btnStart;

    Button btnModalitaSemplice;
    Button btnModalitaAvanzata;

    Button btnReaction;

    Button btnArea1;
    Button btnArea2;
    Button btnArea3;
    Button btnArea4;

    TextView txtStato;
    TextView txtRisultato;

    LinearLayout layoutQuattroAree;

    int modalita = 0;

    int stato = 0;

    int areaCorretta;

    long tempoInizio;


    Handler handler = new Handler(Looper.getMainLooper());

    Random random = new Random();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        btnStart = findViewById(R.id.btnStart);

        btnModalitaSemplice =
                findViewById(R.id.btnModalitaSemplice);

        btnModalitaAvanzata =
                findViewById(R.id.btnModalitaAvanzata);


        btnReaction =
                findViewById(R.id.btnReaction);


        layoutQuattroAree =
                findViewById(R.id.layoutQuattroAree);


        btnArea1 = findViewById(R.id.btnArea1);
        btnArea2 = findViewById(R.id.btnArea2);
        btnArea3 = findViewById(R.id.btnArea3);
        btnArea4 = findViewById(R.id.btnArea4);


        txtStato =
                findViewById(R.id.txtStato);

        txtRisultato =
                findViewById(R.id.txtRisultato);


        btnModalitaSemplice.setOnClickListener(v -> {

            modalita = 0;

            annullaTest();

            btnReaction.setVisibility(View.VISIBLE);

            layoutQuattroAree.setVisibility(View.GONE);

            txtStato.setText(
                    "Modalità semplice"
            );
        });

        btnModalitaAvanzata.setOnClickListener(v -> {

            modalita = 1;

            annullaTest();

            btnReaction.setVisibility(View.GONE);

            layoutQuattroAree.setVisibility(View.VISIBLE);

            txtStato.setText(
                    "Modalità 4 aree"
            );
        });

        btnStart.setOnClickListener(v -> {

            iniziaTest();

        });

        btnReaction.setOnClickListener(v -> {

            if (modalita == 0) {

                controllaPressioneSemplice();

            }

        });

        btnArea1.setOnClickListener(v -> {

            controllaArea(1);

        });

        btnArea2.setOnClickListener(v -> {

            controllaArea(2);

        });

        btnArea3.setOnClickListener(v -> {

            controllaArea(3);

        });

        btnArea4.setOnClickListener(v -> {

            controllaArea(4);

        });
    }


    private void iniziaTest() {

        stato = 1;

        btnStart.setEnabled(false);

        txtRisultato.setText("--- ms");

        txtStato.setText("Aspetta... NON premere!");

        resetColori();

        int ritardo =
                2000 + random.nextInt(3001);

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                mostraSegnale();
            }
        }, ritardo);
    }


    private void mostraSegnale() {

        stato = 2;

        tempoInizio = SystemClock.elapsedRealtime();

        if (modalita == 0) {

            txtStato.setText("PREMI ORA!");

            btnReaction.setText("PREMI!");

            btnReaction.setBackgroundColor(Color.GREEN);

        } else {

            txtStato.setText("PREMI L'AREA VERDE!");

            areaCorretta = random.nextInt(4) + 1;

            switch (areaCorretta) {
                case 1:
                    btnArea1.setBackgroundColor(Color.GREEN);
                    break;

                case 2:
                    btnArea2.setBackgroundColor(Color.GREEN);
                    break;

                case 3:
                    btnArea3.setBackgroundColor(Color.GREEN);
                    break;

                case 4:
                    btnArea4.setBackgroundColor(Color.GREEN);
                    break;
            }
        }
    }


    private void controllaPressioneSemplice() {
        if (stato == 1) {
            troppoPresto();
            return;
        }

        if (stato == 2) {
            calcolaTempo();
        }
    }


    private void controllaArea(int areaPremuta) {
        if (modalita != 1) {
            return;
        }

        if (stato == 1) {

            troppoPresto();

            return;
        }


        if (stato == 2) {

            if (areaPremuta == areaCorretta) {

                calcolaTempo();

            } else {

                txtStato.setText(
                        "Area sbagliata!"
                );

                txtRisultato.setText(
                        "Tentativo non valido"
                );

                stato = 0;

                btnStart.setEnabled(true);

                resetColori();
            }
        }
    }


    private void calcolaTempo() {

        long tempoFine =
                SystemClock.elapsedRealtime();


        long tempoReazione =
                tempoFine - tempoInizio;


        txtRisultato.setText(
                tempoReazione + " ms"
        );


        if (tempoReazione < 200) {

            txtStato.setText(
                    "Velocissimo!"
            );

        } else if (tempoReazione < 300) {

            txtStato.setText(
                    "Ottimo!"
            );

        } else if (tempoReazione < 450) {

            txtStato.setText(
                    "Buon risultato!"
            );

        } else {

            txtStato.setText(
                    "Puoi fare meglio!"
            );
        }


        stato = 0;

        btnStart.setEnabled(true);

        resetColori();
    }


    private void troppoPresto() {

        handler.removeCallbacksAndMessages(null);

        stato = 0;


        txtStato.setText("Troppo presto!");

        txtRisultato.setText("Tentativo non valido");


        btnStart.setEnabled(true);

        resetColori();
    }


    private void resetColori() {

        btnReaction.setBackgroundColor(Color.DKGRAY);

        btnReaction.setText("ATTENDI");

        btnArea1.setBackgroundColor(Color.DKGRAY);

        btnArea2.setBackgroundColor(Color.DKGRAY);

        btnArea3.setBackgroundColor(Color.DKGRAY);

        btnArea4.setBackgroundColor(Color.DKGRAY);
    }


    private void annullaTest() {

        handler.removeCallbacksAndMessages(null);

        stato = 0;

        btnStart.setEnabled(true);

        txtRisultato.setText("--- ms");

        resetColori();
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();

        handler.removeCallbacksAndMessages(null);
    }
}
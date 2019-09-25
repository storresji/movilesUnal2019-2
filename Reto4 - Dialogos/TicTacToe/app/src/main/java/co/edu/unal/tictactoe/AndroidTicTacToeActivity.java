package co.edu.unal.tictactoe;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AndroidTicTacToeActivity extends AppCompatActivity {

    private TicTacToeGame mGame; //Lovercase "m" at the begining of all members variables to distinguish themselves from local parameters and variables
    private Button mBoardButtons[];
    private Button mNewGameButton;
    private TextView mInfoTextView;
    private TextView mIieScore;
    private TextView mHumanScore;
    private TextView mComputerScore;
    private boolean gameOver;
    private int[]scores = {0,0,0}; //tie - human - computer

    static final int DIALOG_DIFFICULTY_ID = 0;
    static final int DIALOG_QUIT_ID = 1;
    static final int DIALOG_ABOUT = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBoardButtons = new Button[TicTacToeGame.BOARD_SIZE];
        mBoardButtons[0] = (Button) findViewById(R.id.one);
        mBoardButtons[1] = (Button) findViewById(R.id.two);
        mBoardButtons[2] = (Button) findViewById(R.id.three);
        mBoardButtons[3] = (Button) findViewById(R.id.four);
        mBoardButtons[4] = (Button) findViewById(R.id.five);
        mBoardButtons[5] = (Button) findViewById(R.id.six);
        mBoardButtons[6] = (Button) findViewById(R.id.seven);
        mBoardButtons[7] = (Button) findViewById(R.id.eight);
        mBoardButtons[8] = (Button) findViewById(R.id.nine);

        //mNewGameButton = (Button) findViewById(R.id.new_game);

        mInfoTextView = (TextView) findViewById(R.id.information);
        mIieScore = (TextView) findViewById(R.id.score_tie);
        mHumanScore = (TextView) findViewById(R.id.score_human);
        mComputerScore = (TextView) findViewById(R.id.score_computer);

        mGame = new TicTacToeGame();
        startNewGame();
    }

    private void startNewGame(){
        mGame.clearBoard();

        for(int i=0; i<mBoardButtons.length; i++){
            mBoardButtons[i].setText("");
            mBoardButtons[i].setEnabled(true);
            mBoardButtons[i].setOnClickListener(new ButtonClickListener(i));
        }
        gameOver = false;
        //mNewGameButton.setOnClickListener(new NewGameButtonListener());
        //mNewGameButton.setEnabled(false);
        mInfoTextView.setText("You go first");
    }

    private void setMove(char player, int location){

        mGame.setMove(player, location);
        mBoardButtons[location].setEnabled(false);
        mBoardButtons[location].setText(String.valueOf(player));
        if(player == TicTacToeGame.HUMAN_PLAYER)
            mBoardButtons[location].setTextColor(Color.rgb(0,200,0));
        else
            mBoardButtons[location].setTextColor(Color.rgb(200,0,0));
    }

    public class ButtonClickListener implements View.OnClickListener {

        int location;

        public ButtonClickListener(int location){
            this.location = location;
        }

        public void onClick(View view){
            if(mBoardButtons[location].isEnabled() && !gameOver){
                setMove(TicTacToeGame.HUMAN_PLAYER, location);

                //If no winner yet, let the computer make a move
                int winner = mGame.checkForWinner();
                if(winner == 0){
                    mInfoTextView.setText("It's Android's turn");
                    int move = mGame.getComputerMove();
                    setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                    winner = mGame.checkForWinner();
                }

                if(winner == 0) {
                    updateGame("It's your turn",100,false);
                }
                else if(winner == 1) {
                    //Activar nuevo juego
                    updateGame("It's a tie!", 0, true);
                } else if(winner == 2) { //human

                    updateGame("You won!", 1, true);
                }
                else //winner == 3
                    updateGame("Android won!", 2, true);

            }
        }

        public void updateGame(String message, int scoreIndex, boolean over){
            mInfoTextView.setText(message);
            gameOver = over;
            if(scoreIndex != 100) {
                scores[scoreIndex]++;
                //mNewGameButton.setEnabled(true);
            }

            mIieScore.setText(String.valueOf(scores[0]));
            mHumanScore.setText(String.valueOf(scores[1]));
            mComputerScore.setText(String.valueOf(scores[2]));
        }
    }

    public class NewGameButtonListener implements View.OnClickListener{

        public void onClick(View view){
            startNewGame();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_game:
                startNewGame();
                return true;
            case R.id.ai_difficulty:
                showDialog(DIALOG_DIFFICULTY_ID);
                return true;
            case R.id.quit:
                showDialog(DIALOG_QUIT_ID);
                return true;
            case R.id.about:
                showDialog(DIALOG_ABOUT);
                return true;
        }
        return false;
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        switch(id) {
            case DIALOG_DIFFICULTY_ID:

                builder.setTitle(R.string.difficulty_choose);

                final CharSequence[] levels = {
                        getResources().getString(R.string.difficulty_easy),
                        getResources().getString(R.string.difficulty_harder),
                        getResources().getString(R.string.difficulty_expert)};

                // TODO: Set selected, an integer (0 to n-1), for the Difficulty dialog.
                // selected is the radio button that should be selected.
                int selected = mGame.getDifficultyLevel( ).ordinal();

                builder.setSingleChoiceItems(levels, selected,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                dialog.dismiss();   // Close dialog

                                // TODO: Set the diff level of mGame based on which item was selected.
                                mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.values()[item]);

                                // Display the selected difficulty level
                                Toast.makeText(getApplicationContext(), levels[item],
                                        Toast.LENGTH_SHORT).show();

                                startNewGame();
                            }
                        });
                dialog = builder.create();

                break;
            case DIALOG_QUIT_ID:
                // Create the quit confirmation dialog

                builder.setMessage(R.string.quit_question)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                AndroidTicTacToeActivity.this.finish();
                            }
                        })
                        .setNegativeButton(R.string.no, null);
                dialog = builder.create();

                break;

            case DIALOG_ABOUT:
                // Create the quit confirmation dialog

                builder = new AlertDialog.Builder(this);
                Context context = getApplicationContext();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.about_dialog, null);
                builder.setView(layout);
                builder.setPositiveButton("OK", null);
                dialog = builder.create();
                break;

        }

        return dialog;
    }



}

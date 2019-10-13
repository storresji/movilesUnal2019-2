package co.edu.unal.tictactoe;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

public class AndroidTicTacToeActivity extends AppCompatActivity {

    private TicTacToeGame mGame; //Lovercase "m" at the begining of all members variables to distinguish themselves from local parameters and variables
    private Button mBoardButtons[];
    private Button mNewGameButton;
    //private ToggleButton soundButton;

    private TextView mInfoTextView;
    private TextView mIieScore;
    private TextView mHumanScore;
    private TextView mComputerScore;
    private boolean gameOver;
    private int[]scores = {0,0,0}; //tie - human - computer
    private boolean humanTurn;
    private boolean sound = true;

    MediaPlayer mHumanMediaPlayer;
    MediaPlayer mComputerMediaPlayer;

    private SharedPreferences mPrefs;

    private BoardView mBoardView;
    static final int DIALOG_QUIT_ID = 1;
    static final int DIALOG_ABOUT = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGame = new TicTacToeGame();
        mBoardView = (BoardView) findViewById(R.id.board);
        mBoardView.setGame(mGame);
        // Listen for touches on the board
        mBoardView.setOnTouchListener(mTouchListener);
        mInfoTextView = (TextView) findViewById(R.id.information);
        mIieScore = (TextView) findViewById(R.id.score_tie);
        mHumanScore = (TextView) findViewById(R.id.score_human);
        mComputerScore = (TextView) findViewById(R.id.score_computer);
        //soundButton = (ToggleButton)findViewById(R.id.toggBtn);

        // Restore the scores from the persistent preference data source
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        mBoardView.setColor( mPrefs.getInt( "color_board",  Color.LTGRAY ) );

        sound = mPrefs.getBoolean("sound", true);
        String difficultyLevel = mPrefs.getString("difficulty_level",
                getResources().getString(R.string.difficulty_harder));
        if (difficultyLevel.equals(getResources().getString(R.string.difficulty_easy)))
            mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Easy);
        else if (difficultyLevel.equals(getResources().getString(R.string.difficulty_harder)))
            mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Harder);
        else
            mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Expert);


        startNewGame();
    }

    public void soundToggle(View view) {
        // Is the toggle on?
        boolean on = ((ToggleButton) view).isChecked();

        if (on) {
            sound = true;
        } else {
            sound = false;
        }
    }

    private void startNewGame(){
        mGame.clearBoard();
        mBoardView.invalidate();   // Redraw the board

        /*for(int i=0; i<mBoardButtons.length; i++){
            mBoardButtons[i].setText("");
            mBoardButtons[i].setEnabled(true);
            mBoardButtons[i].setOnClickListener(new ButtonClickListener(i));
        }*/
        gameOver = false;
        humanTurn = true;
        //soundButton.setChecked(sound);
        //mNewGameButton.setOnClickListener(new NewGameButtonListener());
        //mNewGameButton.setEnabled(false);
        mInfoTextView.setText("You go first");
    }

    private boolean setMove(char player, int location) {
        if (mGame.setMove(player, location)) {
            mBoardView.invalidate();   // Redraw the board
            return true;
        }
        return false;
    }

    // Listen for touches on the board
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {

        public boolean onTouch(View v, MotionEvent event) {

            if(!humanTurn)
                return false;
            // Determine which cell was touched
            int col = (int) event.getX() / mBoardView.getBoardCellWidth();
            int row = (int) event.getY() / mBoardView.getBoardCellHeight();
            int pos = row * 3 + col;

            if( !gameOver && setMove( TicTacToeGame.HUMAN_PLAYER, pos ) ){

                // If no winner yet, let the computer make a move
                if(sound)
                    mHumanMediaPlayer.start( );

                int winner = mGame.checkForWinner( );
                if( winner == 0 ) {
                    mInfoTextView.setText(R.string.android_turn);
                    humanTurn = false;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {

                            mInfoTextView.setText(R.string.android_turn);
                            mComputerMove();
                            mBoardView.invalidate();
                            setWinner();

                        }
                    }, 1000);

                }else {
                    setWinner();
                }
            }

            // So we aren't notified of continued events when finger is moved
            return false;
        }
    };


    public void setWinner(){
        int winner = mGame.checkForWinner();
        if(winner == 0)
            mInfoTextView.setText("It's your turn");
        else if(winner == 1){
            updateGame(R.string.result_tie, 0, true);
        } else if(winner == 2) { //human
            updateGame(R.string.result_human_wins, 1, true);
            String defaultMessage = getResources().getString(R.string.result_human_wins);
            mInfoTextView.setText(mPrefs.getString("victory_message", defaultMessage));
        }
        else //winner == 3
            updateGame(R.string.result_android_wins, 2, true);

}
    public void updateGame(int idString, int scoreIndex, boolean over){
        mInfoTextView.setText(idString);
        gameOver = over;
        if(scoreIndex != 100) {
            scores[scoreIndex]++;
            //mNewGameButton.setEnabled(true);
        }

        mIieScore.setText(String.valueOf(scores[0]));
        mHumanScore.setText(String.valueOf(scores[1]));
        mComputerScore.setText(String.valueOf(scores[2]));
    }

    public void mComputerMove( ){
        mInfoTextView.setText( R.string.android_turn );
        int move = mGame.getComputerMove( );
        setMove( TicTacToeGame.COMPUTER_PLAYER, move );
        if(sound)
            mComputerMediaPlayer.start( );
        humanTurn = true;
        //mGame.checkForWinner( );
    }

    @Override
    protected void onResume() {
        super.onResume();

        mHumanMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.human_sound);
        mComputerMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.computer_sound);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mHumanMediaPlayer.release();
        mComputerMediaPlayer.release();
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
            case R.id.settings:
                startActivityForResult(new Intent(this, Settings.class), 0);
                //showDialog(DIALOG_DIFFICULTY_ID);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RESULT_CANCELED) {
            // Apply potentially new settings

            sound = mPrefs.getBoolean("sound", true);

            String difficultyLevel = mPrefs.getString("difficulty_level",
                    getResources().getString(R.string.difficulty_harder));

            if (difficultyLevel.equals(getResources().getString(R.string.difficulty_easy)))
                mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Easy);
            else if (difficultyLevel.equals(getResources().getString(R.string.difficulty_harder)))
                mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Harder);
            else
                mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Expert);

            mBoardView.setColor(mPrefs.getInt("color_board", Color.LTGRAY));
        }
    }
}

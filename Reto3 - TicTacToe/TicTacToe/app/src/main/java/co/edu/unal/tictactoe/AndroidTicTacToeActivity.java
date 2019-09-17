package co.edu.unal.tictactoe;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Bundle;

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

        mNewGameButton = (Button) findViewById(R.id.new_game);

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
        mNewGameButton.setOnClickListener(new NewGameButtonListener());
        mNewGameButton.setEnabled(false);
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
                mNewGameButton.setEnabled(true);
            }

            mIieScore.setText(String.valueOf(scores[0]));
            mHumanScore.setText(String.valueOf(scores[1]));
            mComputerScore.setText(String.valueOf(scores[2]));
        }
    }

    public class NewGameButtonListener implements View.OnClickListener{

        public void onClick(View view){
            startNewGame();
            gameOver = false;
        }
    }



}

package com.rubean.interviewGame.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rubean.interviewGame.R;
import com.rubean.interviewGame.models.MoveActionModel;
import com.rubean.interviewGame.utils.Utilities;

import java.util.ArrayList;
import java.util.Random;

public class UserEntryActivity extends AppCompatActivity {
    private UserEntryViewModel viewModel;
    private RvActionAdapter rvActionAdapter;
    private EditText etUserMove;
    private TextView tvIsOnline;
    private TextView tvGameOverDetail;
    private TextView tvLastTwoMove;
    private Button btnSendCommand;
    private boolean isServiceOnline = false;
    private Group gameOverGroup;
    private  RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_entry_activity);

        viewModel = new ViewModelProvider(this).get(UserEntryViewModel.class);

        setupUI();
        observerViewModels();

        btnSendCommand.setOnClickListener(v -> {
            if (isServiceOnline){
                String cmdText = etUserMove.getText().toString().trim();
                if (cmdText.isEmpty()){
                    Utilities.showToast(getString(R.string.enter_reply),this);
                }else {
                    viewModel.sendUserCommand(cmdText);
                    etUserMove.setText("");
                }
            }else{
                viewModel.bindToGameBotService(this);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.bindToGameBotService(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewModel.unbindGameBotService(this);
    }


    private void setupUI(){
        gameOverGroup = findViewById(R.id.gameOverGroup);
        btnSendCommand =  findViewById(R.id.btnSendCommand);
        etUserMove = findViewById(R.id.etUserMove);
        tvGameOverDetail = findViewById(R.id.tvGameOverDetail);
        tvIsOnline = findViewById(R.id.tvIsBotOnline);
        tvLastTwoMove = findViewById(R.id.tvLastTwoMove);
        recyclerView= findViewById(R.id.rvUserMoves);
        rvActionAdapter = new RvActionAdapter();
        recyclerView.setAdapter(rvActionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager( this));
    }

    private void observerViewModels(){
        viewModel.getActionLiveData().observe(this, moveActionModels -> {
            //We need to submit new object to the list otherwise it will not calculate the diff and ignore the changes.
            rvActionAdapter.submitList(new ArrayList<>(moveActionModels));
            recyclerView.smoothScrollToPosition(rvActionAdapter.getItemCount());
        });

        viewModel.getServiceConnectionLiveData().observe(this,isServiceConnected -> {
            isServiceOnline = isServiceConnected;

            if (isServiceConnected){
                etUserMove.setEnabled(true);
                btnSendCommand.setText(R.string.send);
                tvIsOnline.setBackgroundColor(ContextCompat.getColor(this,R.color.online));
                tvIsOnline.setText(R.string.online);
            }else{
                viewModel.clearUI();
                etUserMove.setEnabled(false);
                btnSendCommand.setText(R.string.connect);
                tvIsOnline.setBackgroundColor(ContextCompat.getColor(this,R.color.offline));
                tvIsOnline.setText(R.string.offline);
            }
        });

        viewModel.getGameOverLiveData().observe(this,reason -> {
            if (reason!=null && !reason.isEmpty()){
                gameOverGroup.setVisibility(View.VISIBLE);
                tvGameOverDetail.setText(reason);
                etUserMove.setEnabled(false);
                btnSendCommand.setVisibility(View.GONE);
                Utilities.showToast(reason,this);

                int length = rvActionAdapter.getCurrentList().size();
                if (length>2){
                    MoveActionModel lastToLastMove = rvActionAdapter.getCurrentList().get(length-2);
                    MoveActionModel lastMove = rvActionAdapter.getCurrentList().get(length-1);

                    StringBuilder lastTwoMoveBuilder = new StringBuilder();
                    lastTwoMoveBuilder.append("Last two steps were as below");
                    lastTwoMoveBuilder.append("\n\n");
                    lastTwoMoveBuilder.append(lastToLastMove.actionOwner);
                    lastTwoMoveBuilder.append(" ");
                    lastTwoMoveBuilder.append(lastToLastMove.actionText);
                    lastTwoMoveBuilder.append("\n");
                    lastTwoMoveBuilder.append(lastMove.actionOwner);
                    lastTwoMoveBuilder.append(" ");
                    lastTwoMoveBuilder.append(lastMove.actionText);
                    tvLastTwoMove.setText(lastTwoMoveBuilder);

                }
            }
        });
    }

}
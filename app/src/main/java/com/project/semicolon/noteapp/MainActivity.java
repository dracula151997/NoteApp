package com.project.semicolon.noteapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.semicolon.noteapp.databinding.MainActivityBinding;
import com.project.semicolon.noteapp.model.NoteResponse;
import com.project.semicolon.noteapp.model.UserResponse;
import com.project.semicolon.noteapp.networking.NoteEndPoint;
import com.project.semicolon.noteapp.networking.RetrofitClient;
import com.project.semicolon.noteapp.utils.MethodHelper;
import com.project.semicolon.noteapp.utils.SharedHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements IMainActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private NoteEndPoint endPoint;
    private CompositeDisposable disposable = new CompositeDisposable();
    private MainActivityBinding binding;
    private NoteAdapter adapter;
    private List<NoteResponse> noteList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setListener(this);

        endPoint = RetrofitClient.getClient(this).create(NoteEndPoint.class);
        adapter = new NoteAdapter();
        initRecyclerView();

        String apiKey = SharedHelper.getFromPref(this, Constant.API_KEY, "");
        if (TextUtils.isEmpty(apiKey)) {
            //register new user
            registerNewUser();
        } else {
            getAllNotes();
        }
    }

    private void initRecyclerView() {
        binding.noteRecycler.setLayoutManager(new LinearLayoutManager(this));
        binding.noteRecycler.setHasFixedSize(true);
        binding.noteRecycler.setItemAnimator(new DefaultItemAnimator());
        binding.noteRecycler.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        binding.noteRecycler.addOnItemTouchListener(new RecyclerTouchListener(this, binding.noteRecycler, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {
                showActionDialog(position);

            }
        }));
        binding.noteRecycler.setAdapter(adapter);
    }

    private void showActionDialog(final int position) {
        CharSequence[] actions = {"Edit", "Delete"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(actions, (dialogInterface, which) -> {
            if (which == 0)
                showNoteDialog(true, noteList.get(position), position);
            else
                deleteNote(noteList.get(position).getId(), position);
        });

        builder.show();
    }

    private void deleteNote(int id, final int position) {
        endPoint.deleteNote(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(MainActivity.this, "The note is deleted",
                                Toast.LENGTH_SHORT).show();

                        noteList.remove(position);
                        adapter.notifyItemRemoved(position);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();


                    }
                });
    }

    private void getAllNotes() {
        endPoint.getAllNotes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<NoteResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(List<NoteResponse> noteResponses) {
                        Log.d(TAG, "onSuccess: note response: " + noteResponses.size());
                        if (noteResponses.isEmpty()) {
                            binding.noItemLayout.setVisibility(View.VISIBLE);
                            return;
                        }

                        noteList.addAll(noteResponses);
                        adapter.setItems(noteList);


                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void registerNewUser() {
        endPoint.register(MethodHelper.generateRandomUUID())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<UserResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(UserResponse userResponse) {
                        String apiKey = userResponse.getApiKey();
                        SharedHelper.saveToPref(MainActivity.this, Constant.API_KEY, apiKey);


                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

    }

    @Override
    public void onFABClicked() {
        showNoteDialog(false, null, -1);
    }


    public void showNoteDialog(final boolean update, final NoteResponse noteResponse, final int position) {
        View view = LayoutInflater.from(this).inflate(R.layout.note_dialog_fragment_layout,
                null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        final EditText noteEditText = view.findViewById(R.id.input_note);
        if (!update)
            noteEditText.setHint(getString(R.string.new_note_msg));

        if (update && noteResponse != null)
            noteEditText.setText(noteResponse.getNote());

        builder.setCancelable(false)
                .setPositiveButton(update ? "Update" : "Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String note = noteEditText.getText().toString();
                        if (TextUtils.isEmpty(note)) {
                            Toast.makeText(MainActivity.this, "Enter a note", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (update && note != null)
                            updateNote(noteResponse.getId(), note, position);
                        else
                            createNewNote(note);


                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void createNewNote(String note) {
        endPoint.createNote(note)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<NoteResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(NoteResponse noteResponse) {
                        Log.d(TAG, "onSuccess: note response: " + noteResponse.toString());
                        noteList.clear();
                        noteList.add(noteResponse);
                        adapter.setItems(noteList);

                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void updateNote(int id, final String note, final int position) {
        endPoint.updateNote(id, note)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(MainActivity.this, "Updated", Toast.LENGTH_SHORT).show();

                        NoteResponse noteResponse = noteList.get(position);
                        noteResponse.setNote(note);

                        noteList.add(position, noteResponse);
                        adapter.notifyItemChanged(position);


                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

    }

    @Override
    protected void onDestroy() {
        disposable.clear();
        disposable.dispose();
        super.onDestroy();
    }
}

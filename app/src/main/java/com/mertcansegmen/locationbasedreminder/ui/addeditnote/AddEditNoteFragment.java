package com.mertcansegmen.locationbasedreminder.ui.addeditnote;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.mertcansegmen.locationbasedreminder.R;
import com.mertcansegmen.locationbasedreminder.model.Note;
import com.mertcansegmen.locationbasedreminder.ui.MainActivity;

import java.util.Date;

public class AddEditNoteFragment extends Fragment {

    public static final String EXTRA_NOTE ="com.mertcansegmen.locationbasedreminder.EXTRA_NOTE";

    private EditText noteEditText;

    private Note currentNote;

    private AddEditNoteFragmentViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_edit_note, container, false);
        setHasOptionsMenu(true);
        noteEditText = v.findViewById(R.id.txt_note);

        viewModel = ViewModelProviders.of(this).get(AddEditNoteFragmentViewModel.class);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentNote = getArguments().getParcelable(EXTRA_NOTE);
        if(currentNote != null) {
            ((MainActivity)requireActivity()).getSupportActionBar().setTitle("Edit Note");
            noteEditText.setText(currentNote.getText());
        }
    }

    private void saveNote() {
        String text = noteEditText.getText().toString().trim();
        if(text.isEmpty()) {
            Toast.makeText(getContext(), "Empty note deleted", Toast.LENGTH_SHORT).show();
            return;
        }
        if(currentNote != null) {
            currentNote.setText(text);
            viewModel.update(currentNote);
        } else {
            Note newNote = new Note(text, new Date());
            viewModel.insert(newNote);
        }
    }

    private void closeKeyboard() {
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.add_edit_note_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                saveNote();
                closeKeyboard();
                requireActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

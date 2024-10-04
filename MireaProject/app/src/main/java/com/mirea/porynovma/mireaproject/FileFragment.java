package com.mirea.porynovma.mireaproject;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mirea.porynovma.mireaproject.db.App;
import com.mirea.porynovma.mireaproject.db.Key;
import com.mirea.porynovma.mireaproject.db.KeysDao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FileFragment extends Fragment {
    private boolean isWork = false;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FileFragment newInstance(String param1, String param2) {
        FileFragment fragment = new FileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_file, container, false);
        EditText fileEdit = view.findViewById(R.id.fileEdit);
        TextView beforeText = view.findViewById(R.id.beforeText);
        TextView afterText = view.findViewById(R.id.afterText);
        Button enbutton = view.findViewById(R.id.enbutton);
        Button debutton = view.findViewById(R.id.debutton);
        SharedPreferences pref = view.getContext().getSharedPreferences("encrypt", Context.MODE_PRIVATE);
        //KeysDao keysDao = App.getInstance().getDatabase().keysDao();


        int storagePermissionStatus1 = ContextCompat.checkSelfPermission(view.getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int storagePermissionStatus2 = ContextCompat.checkSelfPermission(view.getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE);

        if (storagePermissionStatus1 == PackageManager.PERMISSION_GRANTED
                && storagePermissionStatus2 == PackageManager.PERMISSION_GRANTED) {
            isWork = true;
        } else {
            requestPermissions(new String[]{
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 100);
        }

        writeFile("message1", "Hello! This is a message.");
        writeFile("message2", "Greetings! This is another message.");
        writeFile("message3", "Congratulations! Your prize is a message.");
        writeFile("message4", "Goodbye. This is a sad message.");


        enbutton.setOnClickListener(v -> {
            String filename = fileEdit.getText().toString();
            String text = readFile(filename);
            SecretKey key = generateKey();
            String encMsg = new String(encryptMsg(text, key), StandardCharsets.UTF_8);

            writeFile(filename + "_enc", encMsg);
            Key k = new Key();
            k.file = filename + "_enc";
            k.key = key.getEncoded();
            //keysDao.insert(k);

            beforeText.setText(text);
            afterText.setText(encMsg);
        });

        debutton.setOnClickListener(v -> {
            String filename = fileEdit.getText().toString();
            String text = readFile(filename);
            //byte[] key = keysDao.getById(filename).key;

            if (false) {
                Toast.makeText(view.getContext(), "Нет ключа для этого файла", Toast.LENGTH_SHORT).show();
            } else {
                //SecretKey originalKey = new SecretKeySpec(key, 0, key.length, "AES");
                //String decMsg = decryptMsg(text.getBytes(StandardCharsets.UTF_8), originalKey);

                //writeFile(filename + "_dec", decMsg);

                beforeText.setText(text);
                //afterText.setText(decMsg);
            }
        });

        return view;
    }

    public static SecretKey generateKey() {
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed("this is a very random seed".getBytes());
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(256, sr);
            return new SecretKeySpec((kg.generateKey()).getEncoded(), "AES");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String decryptMsg(byte[] cipherText, SecretKey secret) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secret);
            return new String(cipher.doFinal(cipherText));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException |
                 BadPaddingException | InvalidKeyException e) {
            e.printStackTrace();
            return "error";
        }
    }

    public static byte[] encryptMsg(String msg, SecretKey secret) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            return cipher.doFinal(msg.getBytes());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 BadPaddingException | IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeFile(String filename, String text) {
        Log.d("ExternalStorage", "click");
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(path, filename + ".txt");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file.getAbsoluteFile());
            OutputStreamWriter output = new OutputStreamWriter(fileOutputStream);
            output.write(text);
            output.close();
        } catch (IOException e) {
            Log.w("ExternalStorage", "Error writing " + file, e);
        }
    }

    public String readFile(String filename) {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(path, filename + ".txt");
        List<String> lines = new ArrayList<>();
        try {
            FileInputStream fileInputStream = new FileInputStream(file.getAbsoluteFile());
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            Log.w("ExternalStorage", "Error reading " + file, e);
        }
        return String.join("", lines);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            isWork = grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
    }
}
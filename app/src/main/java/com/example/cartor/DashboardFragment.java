package com.example.cartor;
import android.graphics.Bitmap;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ImageDatabaseHelper databaseHelper;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String selectedImagePath;

    public DashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
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
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        Python.start(new AndroidPlatform(getContext()));
        ImageView imageView = view.findViewById(R.id.imageSelect);
        Button selectImageButton = view.findViewById(R.id.imageSelectButton);

        // Set a click listener for the "Select Image" button
        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the image picker intent
                onSelectImageClick();
            }
        });

        databaseHelper = new ImageDatabaseHelper(requireContext());

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectImageClick();
            }
        });

        return view;
    }

    // Method to launch the image picker intent
    private void onSelectImageClick() {
        // Create an intent to pick an image from the gallery
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            selectedImagePath = getPathFromUri(selectedImage);

            // Save the image URI to the database
            saveImageToDatabase(selectedImage);
            callKMeansImageCompression(selectedImagePath);
        }
    }
    private void saveImageToDatabase(Uri imageUri) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ImageDatabaseHelper.COLUMN_IMAGE_URI, imageUri.toString());
        long newRowId = db.insert(ImageDatabaseHelper.TABLE_IMAGES, null, values);
        if (newRowId != -1) {
            // Image saved to the database successfully
            Toast.makeText(requireContext(), "Image saved to the database", Toast.LENGTH_SHORT).show();
        } else {
            // Handle database insertion error
            Toast.makeText(requireContext(), "Failed to save image to the database", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }
    private void callKMeansImageCompression(String imagePath) {
        Python python = Python.getInstance();
        // Import the Python module containing your code
        PyObject module = python.getModule("kmeans");
        // Call the main function of your Python script
        PyObject result = module.callAttr("main", imagePath);
        // Get the compressed image and its size from the result
        PyObject compressedImage = result.get(0);
        PyObject compressedImageSize = result.get(1);

        // Convert the Python objects to Java types
        byte[] compressedImageData = compressedImage.toJava(byte[].class);
        double compressedImageSizeKb = compressedImageSize.toJava(double.class);

        // Display the compressed image in an ImageView
        ImageView compressedImageView = requireView().findViewById(R.id.compressedImageView);
        Bitmap bitmap = BitmapFactory.decodeByteArray(compressedImageData, 0, compressedImageData.length);
        compressedImageView.setImageBitmap(bitmap);

        // Display the compression size in a TextView
        TextView compressionSizeTextView = requireView().findViewById(R.id.compressionSizeTextView);
        compressionSizeTextView.setText("Compression Size: " + compressedImageSizeKb + " KB");
    }
    private String getPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = requireActivity().getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        } else {
            // Handle error
            return null;
        }
    }
}
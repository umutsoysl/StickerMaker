package com.stickers.laks.whatssappforsticker.Activity.ImageCropper;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.stickers.laks.whatssappforsticker.Activity.CreateStickerPacket;
import com.stickers.laks.whatssappforsticker.Activity.DrawImage;
import com.stickers.laks.whatssappforsticker.Database.Database;
import com.stickers.laks.whatssappforsticker.R;
import com.stickers.laks.whatssappforsticker.Util.ConstVariables;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class CropActivity extends Activity
{
    ImageView compositeImageView;
    boolean crop;
    String uri="";
    Button tryAgain,saveSticker;
    Bitmap resultingImage = null;
    private static String imageOwnerflag = null;
    Dialog enteryAuthorDialog;
    public static String id = null;
    public static String position = null;
    public static String positionID = null;
    public static String isNewCreated = null;
    private ProgressDialog pDialog;
    FirebaseStorage storage;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cropview);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            uri = extras.getString(ConstVariables.PATH_STR);
            imageOwnerflag = extras.getString(ConstVariables.ISHEADER_STR);
            id = extras.getString(ConstVariables.STICKER_ID_STR);
            position = extras.getString(ConstVariables.POSITION_STR);
            positionID = extras.getString(ConstVariables.POSITION_ID);
            isNewCreated = extras.getString(ConstVariables.ISNEWCREATED);
        }

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        byte[] byteArray = DrawImage.byteArray;
        resultingImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        compositeImageView = (ImageView) findViewById(R.id.our_imageview);
        tryAgain = (Button) findViewById(R.id.noTryAgain);
        saveSticker = (Button) findViewById(R.id.saveSticker);

        compositeImageView.setImageBitmap(resultingImage);
        pDialog = new ProgressDialog(CropActivity.this);

        tryAgain.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                YoYo.with(Techniques.FlipInX)
                        .duration(800)
                        .playOn(tryAgain);

                Intent i = new Intent(CropActivity.this,DrawImage.class);
                Bundle bundle = new Bundle();
                bundle.putString(ConstVariables.PATH_STR,uri);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        saveSticker.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    YoYo.with(Techniques.FlipInX)
                            .duration(800)
                            .playOn(saveSticker);

                    Database db = new Database(CropActivity.this);

                    if(db.isHasStickerPacket(id)){

                      String root = db.getStickerRootPath(id);
                      String temp[] = root.split(ConstVariables.SLASH);
                      root = "stickers_asset"+String.valueOf(id);
                      saveBitmapFile(root,temp[1],temp[0]);

                    }else
                    {
                        showEntryAuthorDialog();
                    }

                    db.close();

                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });

    }

    public Bitmap getResizedBitmap(Bitmap image)
    {
        return Bitmap.createScaledBitmap(image, ConstVariables.BITMAP_MAX_SIZE, ConstVariables.BITMAP_MAX_SIZE, true);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(CropActivity.this,DrawImage.class);
        Bundle bundle = new Bundle();
        bundle.putString(ConstVariables.PATH_STR,uri);
        bundle.putString(ConstVariables.ISHEADER_STR,isNewCreated);
        bundle.putString(ConstVariables.ISHEADER_STR,imageOwnerflag);
        bundle.putString(ConstVariables.POSITION_STR,position);
        bundle.putString(ConstVariables.POSITION_ID,positionID);
        i.putExtras(bundle);
        startActivity(i);
    }

    public void saveBitmapFile(String root, String name , String author) throws IOException
    {
               onDestroy();
                Bitmap bitmap = resultingImage;
                bitmap = getResizedBitmap(bitmap);
                int h = bitmap.getHeight();
                int w = bitmap.getWidth();
                ContextWrapper cw = new ContextWrapper(CropActivity.this);
                File directory = cw.getDir(root, Context.MODE_PRIVATE);

                String fileName = ConstVariables.EMPTY;
                if(imageOwnerflag.equals(ConstVariables.STR_ONE)){
                    fileName = ConstVariables.TROYICON_NAME+".png";
                }else{
                    fileName = position+".png";
                }

                dirChecker(CropActivity.this.getFilesDir()+"/"+id);
               // Uri troyUri = Uri.parse(CropActivity.this.getFilesDir()+"/"+id+"/"+ConstVariables.TROYICON_NAME+".png");
                String path = CropActivity.this.getFilesDir()+"/"+id+"/"+fileName;
              //  Uri urix =  Uri.parse(CropActivity.this.getFilesDir()+"/"+id+"/"+fileName);
                Log.w("Conversion Data: ", "path: " + path);

                File mypath = new File(directory, fileName);
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(mypath);
                    bitmap.compress(Bitmap.CompressFormat.PNG, ConstVariables.IMAGE_QUALITY, fos);
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fos.close();

                        Date c = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat(ConstVariables.DATE_FORMAT);
                        String createDate = df.format(c);

                        String[] fireUrl = uploadImage(ConstVariables.FİLE_HEADER_STR+mypath.getPath(),root,name,author,createDate);



                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


    }

    private static void dirChecker(String dir) {
        File f = new File(dir);
        if (!f.isDirectory()) {
            f.mkdirs();
        }
    }

    public void showEntryAuthorDialog()
    {

        enteryAuthorDialog = new Dialog(CropActivity.this);
        enteryAuthorDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        enteryAuthorDialog.setContentView(R.layout.sticker_author_entry_dialog);


        enteryAuthorDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Window window = enteryAuthorDialog.getWindow();
        WindowManager.LayoutParams param = window.getAttributes();
        param.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
        enteryAuthorDialog.setCanceledOnTouchOutside(true);

        enteryAuthorDialog.setCanceledOnTouchOutside(false);
        enteryAuthorDialog.setCancelable(true);

        View dis = (View) enteryAuthorDialog.findViewById(R.id.arkaplan);
        RelativeLayout kapat = (RelativeLayout) enteryAuthorDialog.findViewById(R.id.kapat);
        RelativeLayout ok = (RelativeLayout) enteryAuthorDialog.findViewById(R.id.gonder);
        final EditText author = (EditText) enteryAuthorDialog.findViewById(R.id.author);
        final EditText name = (EditText) enteryAuthorDialog.findViewById(R.id.name);
        final ImageView sticker = (ImageView) enteryAuthorDialog.findViewById(R.id.troyIcon);

        sticker.setImageBitmap(resultingImage);

        kapat.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                enteryAuthorDialog.dismiss();
            }
        });


        ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(name.getText().toString().trim().length()>2 && author.getText().toString().trim().length()>2){

                    String root = "stickers_asset"+String.valueOf(id);
                    try
                    {
                        saveBitmapFile(root,name.getText().toString().trim(),author.getText().toString().trim());
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });

        enteryAuthorDialog.show();

    }

    private String[] uploadImage(String filePath,String root,String name,String author,String createDate) {

        final String[] path = {null};
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("sticker/"+root+"/"+ UUID.randomUUID().toString());
            ref.putFile(Uri.parse(filePath))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> downloadUri = ref.getDownloadUrl();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                            {
                                path[0] = downloadUri.getResult().normalizeScheme().toString();
                            }

                            if(path[0]!=null)
                            {
                                Database db = new Database(CropActivity.this);

                                if (db.isHasStickerPacket(id) && imageOwnerflag.equals(ConstVariables.STR_ONE))
                                {
                                    db.updateStickerPathTroy(id, path[0]);
                                    startActivity(new Intent(CropActivity.this, CreateStickerPacket.class));
                                }
                                else if (positionID != null && !positionID.isEmpty() && db.isHasStickerPacket(id) && imageOwnerflag.equals(ConstVariables.STR_ZERO))
                                {

                                    db.updateStickerPath(id, path[0], Integer.parseInt(positionID));
                                    startActivity(new Intent(CropActivity.this, CreateStickerPacket.class));
                                }
                                else
                                {
                                    db.addStickerPacket(id, name, author, path[0], createDate, imageOwnerflag);

                                    if (isNewCreated != null && !isNewCreated.isEmpty() && isNewCreated.equals(ConstVariables.STR_ZERO))
                                    {
                                        db.addStickerPacketInfo(id, name, author);
                                    }

                                    startActivity(new Intent(CropActivity.this, CreateStickerPacket.class));
                                }

                                db.close();
                            }

                            progressDialog.dismiss();
                            Toast.makeText(CropActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(CropActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Creating Sticker"+(int)progress+"%");
                        }
                    });
        }

        return  path;
    }



    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        enteryAuthorDialog.dismiss();
        enteryAuthorDialog.cancel();
        enteryAuthorDialog = null;
    }
}
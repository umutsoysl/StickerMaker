package com.stickers.laks.whatssappforsticker.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.stickers.laks.whatssappforsticker.Adapter.CropStickerAdapter;
import com.stickers.laks.whatssappforsticker.BuildConfig;
import com.stickers.laks.whatssappforsticker.Database.Database;
import com.stickers.laks.whatssappforsticker.GoogleKeyboard.AppIndexingUpdateService;
import com.stickers.laks.whatssappforsticker.R;
import com.stickers.laks.whatssappforsticker.Util.ConstVariables;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import in.srain.cube.views.GridViewWithHeaderAndFooter;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CreateStickerPacket extends Activity
{
    GridViewWithHeaderAndFooter createStickerList;
    CropStickerAdapter cropStickerAdapter;
    String[] position;
    ImageView chooseImage,setImageView;
    RelativeLayout chooseImageLyt;
    public static String id;
    public static String pozisyon = ConstVariables.STR_ZERO;
    ArrayList<HashMap<String, String>> stickerPathList;
    String[] stickerPositionId;
    String isHeader = ConstVariables.STR_ZERO;
    String isNewCreated = ConstVariables.STR_ZERO;
    String[] stickerId;
    ImageButton back;
    RelativeLayout addWhatsApp;
    public static View addWpLoading;
    @Override
    protected void onResume()
    {
        super.onResume();
        getStickerPathList();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_create_sticker_packet);
        LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        createStickerList = (GridViewWithHeaderAndFooter)findViewById(R.id.myStickerList);
        View headerView = layoutInflater.inflate(R.layout.create_packet_list_header, null);
        chooseImage = (ImageView) headerView.findViewById(R.id.choose_image);
        setImageView = (ImageView) headerView.findViewById(R.id.setImage);
        chooseImageLyt = (RelativeLayout) headerView.findViewById(R.id.stickerHeaderIcon);
        back = (ImageButton) findViewById(R.id.backPage);
        addWhatsApp = (RelativeLayout) findViewById(R.id.addWhatsApp);
        addWpLoading = findViewById(R.id.entry_activity_progress);
        createStickerList.addHeaderView(headerView);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getString(ConstVariables.STICKER_ID_STR);
        }

        chooseImageLyt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                isHeader = ConstVariables.STR_ONE;
                startActivityForResult(resimSecimiIntent(), ConstVariables.REQUEST_SELECT_IMAGE);
            }
        });

        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(CreateStickerPacket.this,MainActivity.class);
                startActivity(i);
            }
        });


        createStickerList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                pozisyon = String.valueOf(position);
                startActivityForResult(resimSecimiIntent(), ConstVariables.REQUEST_SELECT_IMAGE);
            }
        });

        addWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppIndexingUpdateService.enqueueWork(CreateStickerPacket.this,id);
            }
        });
    }


    public Intent resimSecimiIntent() {

        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");

        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "pickImageResult.jpeg"));
        }
        return outputFileUri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ConstVariables.REQUEST_SELECT_IMAGE) {
                Uri imageUri = getPickImageResultUri(data);
                startCropActivity(imageUri);
            }

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (result.getUri() != null) {

                    Uri uri = result.getUri();
                   /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    {
                        getContentResolver().takePersistableUriPermission(Objects.requireNonNull(uri), Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }*/

                    Intent i = new Intent(CreateStickerPacket.this,DrawImage.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(ConstVariables.PATH_STR,uri.getPath());
                    bundle.putString(ConstVariables.ISHEADER_STR,isHeader);
                    bundle.putString(ConstVariables.STICKER_ID_STR,id);
                    if(stickerPositionId.length == 0 || stickerPositionId.length < Integer.parseInt(pozisyon) ){
                        bundle.putString(ConstVariables.POSITION_ID,null);
                    }else{
                        bundle.putString(ConstVariables.POSITION_ID,stickerPositionId[Integer.parseInt(pozisyon)]);
                    }
                    bundle.putString(ConstVariables.POSITION_STR,(pozisyon));
                    bundle.putString(ConstVariables.ISNEWCREATED,(isNewCreated));
                    i.putExtras(bundle);
                    startActivity(i);
                    //Picasso.with(this).load(result.getUri()).into(setImageView);
                } else {
                    Toast.makeText(CreateStickerPacket.this, "Fotoğraf seçilemedi!", Toast.LENGTH_LONG).show();
                }

            }
        }
    }

    public Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null && data.getData() != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }
        return isCamera ? getCaptureImageOutputUri() : data.getData();
    }

    private void startCropActivity(Uri imgUri) {
        CropImage.activity(imgUri)
                .setAutoZoomEnabled(true)
                .setAspectRatio(400, 400)
                .setFixAspectRatio(true)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    public void getStickerPathList(){

        Database db = new Database(CreateStickerPacket.this);

        stickerPathList = db.getStickersPath(id);
        int size = stickerPathList.size();
        stickerPositionId = new String[size];

        List<String> imagesPath = new ArrayList<String>();
        int removeIndex = 99;
        position = new String[size];
        stickerId = new String[size];

        for (int i = 0; i < size; i++)
        {
            isNewCreated = ConstVariables.STR_ONE;
            position[i] = String.valueOf(i+1);
            imagesPath.add(stickerPathList.get(size - i - 1).get("path"));
            stickerId[i] = stickerPathList.get(size - i - 1).get("id");
            if(stickerPathList.get(size - i - 1).get("path").contains("troyIcon")){
                removeIndex = i;
            }

        }

        if(removeIndex != 99){
            // Picasso.with(CreateStickerPacket.this).load(imagesPath.get(removeIndex)).into(setImageView);
           // setImage(imagesPath.get(removeIndex),setImageView);
            Picasso.with(CreateStickerPacket.this).load(imagesPath.get(removeIndex)).into(setImageView);
                    imagesPath.remove(removeIndex);
            chooseImage.setVisibility(View.INVISIBLE);
        }else{
            chooseImage.setVisibility(View.VISIBLE);
        }

        cropStickerAdapter = new CropStickerAdapter(CreateStickerPacket.this, imagesPath, position, id, stickerId);
        createStickerList.setAdapter(cropStickerAdapter);
        cropStickerAdapter.notifyDataSetChanged();

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(CreateStickerPacket.this,MainActivity.class);
        startActivity(i);
    }

    public void setImage(final String url, final ImageView imageview){

        Picasso.with(CreateStickerPacket.this).load(url).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                {
                    imageview.setBackground(new BitmapDrawable(bitmap));
                }else{
                    Picasso.with(CreateStickerPacket.this).load(url).into(imageview);
                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }




}

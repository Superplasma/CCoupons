package com.example.administrator.ccoupons.User;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ccoupons.R;
import com.example.administrator.ccoupons.Tools.DataBase.LoginInformationManager;
import com.example.administrator.ccoupons.Tools.TakePhotoUtil;
import com.jph.takephoto.model.TResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserPortraitActivity extends AppCompatActivity {
    private TakePhotoUtil takePhotoUtil;
    private LoginInformationManager informationManager;
    private ImageView portrait;
    private LinearLayout bg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_portrait);
        initView();

        takePhotoUtil = new TakePhotoUtil(this);
        if (useTakePhoto()) {
            takePhotoUtil.onCreate(savedInstanceState);
        }
        initPortrait();
        setOnLongClickListeners();
    }

    private void setOnLongClickListeners() {
        bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.noanim, R.anim.portrait_out);
            }
        });

        bg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.portrait_bottom_dialog, null);
                TextView tv_account = (TextView) view.findViewById(R.id.tv_take_photo);
                TextView tv_compare = (TextView) view.findViewById(R.id.tv_from_album);
                final Dialog mBottomSheetDialog = new Dialog(UserPortraitActivity.this, R.style.MaterialDialogSheet);
                mBottomSheetDialog.setContentView(view);
                mBottomSheetDialog.setCancelable(true);
                mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
                mBottomSheetDialog.show();
                tv_account.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(UserPortraitActivity.this, "拍照", Toast.LENGTH_SHORT).show();
                        takePhotoUtil.takePhoto(TakePhotoUtil.Select_type.PICK_BY_TAKE, new TakePhotoUtil.SimpleTakePhotoListener() {
                            @Override
                            public void takeSuccess(TResult result) {
                                String s = result.getImage().getCompressPath();
                                System.out.println(s);
                                Bitmap bitmap = BitmapFactory.decodeFile(s);
                                portrait.setImageBitmap(bitmap);
                                updatePortrait(s);
                            }
                        });
                        mBottomSheetDialog.dismiss();
                    }
                });
                tv_compare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(UserPortraitActivity.this, "从相册中选择", Toast.LENGTH_SHORT).show();
                        takePhotoUtil.takePhoto(TakePhotoUtil.Select_type.PICK_BY_SELECT, new TakePhotoUtil.SimpleTakePhotoListener() {
                            @Override
                            public void takeSuccess(TResult result) {
                                String s = result.getImage().getCompressPath();
                                System.out.println(s);
                                Bitmap bitmap = BitmapFactory.decodeFile(s);
                                portrait.setImageBitmap(bitmap);
                                updatePortrait(s);
                            }
                        });
                        mBottomSheetDialog.dismiss();
                    }
                });
                return true;
            }
        });
    }

    private void initView() {
        informationManager = new LoginInformationManager(this);
        portrait = (ImageView) findViewById(R.id.user_portrait_view);
        //portrait.setImageResource(DataHolder.User.portraitId);
        bg = (LinearLayout) findViewById(R.id.portrait_bg);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (useTakePhoto()) {
            takePhotoUtil.onSaveInstanceState(outState);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (useTakePhoto()) {
            takePhotoUtil.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (useTakePhoto()) {
            takePhotoUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    protected boolean useTakePhoto() {
        return true;
    }

    public void updatePortrait(String path) {
        Pattern pat = Pattern.compile("(portrait_)([0-9]+)(.jpg)");
        Matcher mat = pat.matcher(path);
        boolean rs = mat.find();
        Long millis = Long.parseLong(mat.group(2));
        informationManager.setPortraitPath(path);
        //上传Millis和图片到服务器
    }

    public void initPortrait() {
        String s = informationManager.getPortraitPath();
        if (s != "") {
            Bitmap bitmap = BitmapFactory.decodeFile(s);
            portrait.setImageBitmap(bitmap);
        } else portrait.setImageResource(R.drawable.testportrait);
    }
}
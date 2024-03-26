package com.example.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.load.model.ModelLoaderRegistry;
import com.example.appbanhang.R;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class HinhAnh3DActivity extends AppCompatActivity {
    Toolbar toolbar;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ModelLoaderRegistry ModelRenderable = new ModelLoaderRegistry(null);
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hinh_anh3_dactivity);
        initView();
        ActionToolbar();
//        getHinhAnh3D();
    }
//    private void getHinhAnh3D() {
//        ModelRenderable.builder()
//                .setSource(this, Uri.parse("model.sfb"))
//                .build()
//                .thenAccept { modelRenderable ->
//                val anchor = imageView.arSceneView.session.createAnchor(imageView.arSceneView.center)
//            val anchorNode = AnchorNode(anchor)
//            anchorNode.renderable = modelRenderable
//            arFragment.arSceneView.scene.addChild(anchorNode)
//        }
//            .exceptionally { throwable ->
//            // Handle error loading model
//            return@exceptionally null
//        }
//    }

    private void ActionToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void initView() {
        toolbar = findViewById(R.id.toobar3d);
        imageView = findViewById(R.id.img3d);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
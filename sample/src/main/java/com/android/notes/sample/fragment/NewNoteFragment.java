package com.android.notes.sample.fragment;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.notes.sample.R;
import com.android.notes.sample.app.AppBaseFragment;
import com.android.notes.sample.manager.BaseManager;
import com.android.notes.sample.model.AutoModel;
import com.android.notes.sample.utils.SkinSelectorUtils;
import com.lib.utils.ValidUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class NewNoteFragment extends AppBaseFragment implements AdapterView.OnItemSelectedListener {
    private String[] starArray = {"Work and study", "Life", "Health and well"};
    private int type;

    @BindView(R.id.relative_layout_bg)
    RelativeLayout relativeLayoutBg;
    @BindView(R.id.relative_layout_top)
    RelativeLayout relativeLayoutTop;
    @BindView(R.id.relative_bottom)
    RelativeLayout relativeBottom;
    @BindView(R.id.relative_button)
    RelativeLayout relativeButton;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.linear_layout_spinner)
    LinearLayout linearLayoutSpinner;

    @BindView(R.id.relative_layout_edit)
    LinearLayout linearLayoutEditContent;
    @BindView(R.id.edit_text_content)
    EditText editTextContent;
    @BindView(R.id.text_view_num)
    TextView textViewNum;
    @BindView(R.id.text_view_error_input)
    TextView textViewErrorInput;

    public static NewNoteFragment newInstance() {
        NewNoteFragment fragment = new NewNoteFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSetupView() {
        super.onSetupView();
        setLayoutView(R.layout.fragment_newnote);
    }

    @Override
    public void onBindData() {
        super.onBindData();
        baseManager = new BaseManager(getContext());
        init();
        initSkinColor();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        type = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // 当没有选择任何选项时的处理
    }

    @OnClick({
            R.id.relative_layout_close,
            R.id.image_view_back,
            R.id.relative_button
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.relative_layout_close:
            case R.id.image_view_back:
                onBackPressed();
                break;
            case R.id.relative_button:
                checkData();
                break;
        }
    }

    @Override
    public void onClickButtonRetry(Object tag) {
        super.onClickButtonRetry(tag);
    }

    public void init() {
        //声明一个下拉列表的数组适配器
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.item_select, starArray);
        // 设置下拉框的样式
        adapter.setDropDownViewResource(R.layout.item_dropdown);

        // 将适配器应用到Spinner控件
        spinner.setAdapter(adapter);

        // 设置下拉框的选项选择监听器
        spinner.setOnItemSelectedListener(this);

        editTextContent.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = charSequence.toString();
                if (!ValidUtil.isEmpty(str)) {
                    textViewNum.setVisibility(View.VISIBLE);
                    textViewNum.setText(str.length() + "/200");
                    if (str.length() > 200) {
                        editTextContent.setText(str.substring(0, 200)); //截取前x位
                        editTextContent.requestFocus();
                        editTextContent.setSelection(editTextContent.getText().length()); //光标移动到最后
                        textViewNum.setText("200/200");
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void checkData() {
        String content = editTextContent.getText().toString();
        AutoModel model = new AutoModel();
        if (!isEmptyContent(content)) {
            model.setContent(content);
            switch (type) {
                case 0:
                    List workList = baseManager.getWorkList();
                    if (ValidUtil.isEmpty(workList)) {
                        List dataList = new ArrayList();
                        dataList.add(model);
                        baseManager.setWorkList(dataList);
                    } else {
                        workList.add(model);
                        baseManager.setWorkList(workList);
                    }
                    onBackPressed();
                    break;
                case 1:
                    List lifeList = baseManager.getLifeList();
                    if (ValidUtil.isEmpty(lifeList)) {
                        List dataList = new ArrayList();
                        dataList.add(model);
                        baseManager.setLifeList(dataList);
                    } else {
                        lifeList.add(model);
                        baseManager.setLifeList(lifeList);
                    }
                    onBackPressed();
                    break;
                case 2:
                    List wellList = baseManager.getWellList();
                    if (ValidUtil.isEmpty(wellList)) {
                        List dataList = new ArrayList();
                        dataList.add(model);
                        baseManager.setWellList(dataList);
                    } else {
                        wellList.add(model);
                        baseManager.setWellList(wellList);
                    }
                    onBackPressed();
                    break;
            }


        }
    }

    private boolean isEmptyContent(String content) {
        if (ValidUtil.isEmpty(content)) {
            textViewErrorInput.setVisibility(View.VISIBLE);
            return true;
        }
        textViewErrorInput.setVisibility(View.GONE);
        return false;
    }

    public void initSkinColor() {
        SkinSelectorUtils.setCorner(relativeLayoutTop, 0, 0, 35, 35, "290945");
        SkinSelectorUtils.setGradientTl_Br(relativeLayoutBg, "1d244f", "3f1a47");
        SkinSelectorUtils.setCorner(relativeBottom, 35, 35, 0, 0, "000000");
        SkinSelectorUtils.setCorner(relativeButton, 100, 100, 100, 100, "f54085");
        SkinSelectorUtils.setGradientAndCornerAndStroker(linearLayoutSpinner, 50, "2f2b5c", "3f1e61", "404040", GradientDrawable.Orientation.LEFT_RIGHT);
        SkinSelectorUtils.setGradientAndCornerAndStroker(linearLayoutEditContent, 50, "2f2b5c", "3f1e61", "404040", GradientDrawable.Orientation.LEFT_RIGHT);
    }
}
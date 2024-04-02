package com.android.notes.sample.manager;

import android.content.Context;

import com.android.notes.sample.BuildConfig;
import com.android.notes.sample.model.AutoModel;
import com.lib.base.BaseView;
import com.lib.retrofit.RetrofitError;
import com.lib.utils.ParseUtil;
import com.lib.utils.ValidUtil;
import com.ywvision.wyvisionhelper.api.ApiMethod;
import com.ywvision.wyvisionhelper.app.AppBasePresenter;
import com.ywvision.wyvisionhelper.app.AppBaseRequest;
import com.ywvision.wyvisionhelper.app.AppSharedPref;

import static com.ywvision.wyvisionhelper.app.AppConstants.SP_LANGUAGE;

import java.util.List;

public class BaseManager<T extends BaseView> extends AppBasePresenter<T> {

    private DbManager dbManager;
    private AppSharedPref sharedPref;
    private AppSharedPref sharedPrefOther;
    private boolean isOther;

    public BaseManager(Context context) {
        this(context, null);
    }

    public BaseManager(Context context, T view) {
        this(context, view, false);
    }

    public BaseManager(Context context, T view, boolean isOther) {
        super(context, view);
        dbManager = new DbManager(context);
        sharedPref = new AppSharedPref(context);
        sharedPrefOther = new AppSharedPref(context, SP_LANGUAGE);
        this.isOther = isOther;
    }

    public AppSharedPref getSharedPref() {
        return sharedPref;
    }

    public void removeView() {
        view = null;
    }

    // Main
    public <Q extends AppBaseRequest> void callApi(final String urlEndPoint,
                                                   final Q request,
                                                   final Class responseType,
                                                   final Object... params) {
       // callApi(String.valueOf(BuildConfig.VERSION_CODE), SYSTEM_STATUS, getLanguageCode(), urlEndPoint, request, responseType, params);
    }

    public <Q extends AppBaseRequest> void callApiNormal(final String urlEndPoint,
                                                         final Q request,
                                                         final Class responseType,
                                                         final Object... params) {
       // callApiNormal(String.valueOf(BuildConfig.VERSION_CODE), SYSTEM_STATUS, getLanguageCode(), urlEndPoint, request, responseType, params);
    }




    @Override
    public void onResponse(String urlEndPoint, Object data, Object... params) {
        switch (urlEndPoint) {

            case ApiMethod.API_POST_APP_LOGIN:

                break;
        }
    }

    @Override
    public void responseError(RetrofitError error, Object... params) {
        switch (error.getUrl()) {
//            case ApiMethod.API_DEL_CART_ITEM_INC:
//            case ApiMethod.API_DEL_CART_ITEM_DEC:
//                int cartxPosition = (int) params[0];
//                int count = (int) params[1];
//                ((GetCartView) view).onErrorUpdateCartCount(cartxPosition, count);
//                break;
            default:
                super.responseError(error, params);
                break;
        }
    }

    public void clearSharedPref() {
        sharedPref.clear();
    }

    public List<AutoModel> getWorkList() {
        return sharedPref.get(AppSharedPref.Key.WORK, null);
    }

    public void setWorkList(List<AutoModel> workList) {
        if (!ValidUtil.isEmpty(workList)) {
            sharedPref.set(AppSharedPref.Key.WORK, workList);
        }
    }

    public List<AutoModel> getLifeList() {
        return sharedPref.get(AppSharedPref.Key.LIFE, null);
    }

    public void setLifeList(List<AutoModel> lifeList) {
        if (!ValidUtil.isEmpty(lifeList)) {
            sharedPref.set(AppSharedPref.Key.LIFE, lifeList);
        }
    }

    public List<AutoModel> getWellList() {
        return sharedPref.get(AppSharedPref.Key.HEALTH, null);
    }

    public void setWellList(List<AutoModel> wellList) {
        if (!ValidUtil.isEmpty(wellList)) {
            sharedPref.set(AppSharedPref.Key.HEALTH, wellList);
        }
    }
}

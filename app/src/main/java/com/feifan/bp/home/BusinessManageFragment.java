package com.feifan.bp.home;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Response;
import com.feifan.bp.OnFragmentInteractionListener;
import com.feifan.bp.PlatformState;
import com.feifan.bp.R;
import com.feifan.bp.home.Model.MenuListModel;
import com.feifan.bp.home.Model.MenuModel;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BusinessManageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BusinessManageFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BusinessManageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BusinessManageFragment newInstance() {
        BusinessManageFragment fragment = new BusinessManageFragment();
        return fragment;
    }

    public BusinessManageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_business_manage, container, false);
        final ListView list = (ListView)v.findViewById(R.id.manage_menu_list);

        HomeCtrl.fetchManageMenus(PlatformState.getInstance().getUserProfile().getAgId(), new Response.Listener<MenuListModel>() {
            @Override
            public void onResponse(MenuListModel menuListModel) {
                Log.e("xuchunlei", menuListModel.toString());
                MenuAdapter adapter = new MenuAdapter(getActivity(), menuListModel);
                list.setAdapter(adapter);
            }
        });

        return v;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


}

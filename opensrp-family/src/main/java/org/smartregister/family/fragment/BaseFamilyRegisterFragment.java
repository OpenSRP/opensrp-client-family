package org.smartregister.family.fragment;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.cursoradapter.RecyclerViewPaginatedAdapter;
import org.smartregister.family.R;
import org.smartregister.family.activity.BaseFamilyRegisterActivity;
import org.smartregister.family.contract.FamilyRegisterFragmentContract;
import org.smartregister.family.provider.FamilyRegisterProvider;
import org.smartregister.family.util.Constants;
import org.smartregister.family.util.DBConstants;
import org.smartregister.family.util.Utils;
import org.smartregister.view.activity.BaseRegisterActivity;
import org.smartregister.view.customcontrols.CustomFontTextView;
import org.smartregister.view.customcontrols.FontVariant;
import org.smartregister.view.fragment.BaseRegisterFragment;

import java.util.Set;

public abstract class BaseFamilyRegisterFragment extends BaseRegisterFragment implements FamilyRegisterFragmentContract.View {
    private static final String TAG = BaseFamilyRegisterFragment.class.getCanonicalName();
    public static final String CLICK_VIEW_NORMAL = "click_view_normal";
    public static final String CLICK_VIEW_DOSAGE_STATUS = "click_view_dosage_status";

    @Override
    public void initializeAdapter(Set<org.smartregister.configurableviews.model.View> visibleColumns) {
        FamilyRegisterProvider familyRegisterProvider = new FamilyRegisterProvider(getActivity(), commonRepository(), visibleColumns, registerActionHandler, paginationViewHandler);
        clientAdapter = new RecyclerViewPaginatedAdapter(null, familyRegisterProvider, context().commonrepository(this.tablename));
        clientAdapter.setCurrentlimit(20);
        clientsView.setAdapter(clientAdapter);
    }

    @Override
    public void setupViews(View view) {
        super.setupViews(view);

        // Update top left icon
        qrCodeScanImageView = view.findViewById(R.id.scanQrCode);
        if (qrCodeScanImageView != null) {
            qrCodeScanImageView.setVisibility(View.GONE);
        }

        ImageView leftMenu = view.findViewById(R.id.left_menu);
        if (leftMenu != null) {
            leftMenu.setVisibility(View.VISIBLE);
        }

        // Update Search bar
        View searchBarLayout = view.findViewById(R.id.search_bar_layout);
        searchBarLayout.setBackgroundResource(R.color.customAppThemeBlue);

        if (getSearchView() != null) {
            getSearchView().setBackgroundResource(R.color.white);
            getSearchView().setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_search, 0, 0, 0);
        }

        // Update sort filter
        TextView filterView = view.findViewById(R.id.filter_text_view);
        if (filterView != null) {
            filterView.setText(getString(R.string.sort));
        }

        // Update title name
        ImageView logo = view.findViewById(R.id.opensrp_logo_image_view);
        if (logo != null) {
            logo.setVisibility(View.GONE);
        }

        CustomFontTextView titleView = view.findViewById(R.id.txt_title_label);
        if (titleView != null) {
            titleView.setVisibility(View.VISIBLE);
            titleView.setText(getString(R.string.all_families));
            titleView.setFontVariant(FontVariant.REGULAR);
        }
    }

    @Override
    protected void refreshSyncProgressSpinner() {
        super.refreshSyncProgressSpinner();
        if(syncButton != null) {
            syncButton.setVisibility(View.GONE);
        }
    }

    @Override
    protected void startRegistration() {
        ((BaseFamilyRegisterActivity) getActivity()).startFormActivity(Utils.metadata().familyRegister.formName, null, null);
    }


    @Override
    public void showNotFoundPopup(String uniqueId) {
        if (getActivity() == null) {
            return;
        }
        NoMatchDialogFragment.launchDialog((BaseRegisterActivity) getActivity(), DIALOG_TAG, uniqueId);
    }

    @Override
    public void setUniqueID(String s) {
        if (getSearchView() != null) {
            getSearchView().setText(s);
        }
    }

    @Override
    protected void onViewClicked(View view) {

        if (getActivity() == null) {
            return;
        }

        if (view.getTag() != null && view.getTag(R.id.VIEW_ID) == CLICK_VIEW_NORMAL) {
            goToPatientDetailActivity((CommonPersonObjectClient) view.getTag(), false);
        } else if (view.getTag() != null && view.getTag(R.id.VIEW_ID) == CLICK_VIEW_DOSAGE_STATUS) {
            CommonPersonObjectClient pc = (CommonPersonObjectClient) view.getTag();
            String baseEntityId = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.BASE_ENTITY_ID, true);

            if (StringUtils.isNotBlank(baseEntityId)) {
                // TODO Proceed to dose status
            }
        }
    }

    private void goToPatientDetailActivity(CommonPersonObjectClient patient,
                                           boolean launchDialog) {
        if (launchDialog) {
            Log.i(BaseFamilyRegisterFragment.TAG, patient.name);
        }

        Intent intent = new Intent(getActivity(), Utils.metadata().profileActivity);
        intent.putExtra(Constants.INTENT_KEY.BASE_ENTITY_ID, patient.getCaseId());
        startActivity(intent);
    }

    @Override
    public FamilyRegisterFragmentContract.Presenter presenter() {
        return (FamilyRegisterFragmentContract.Presenter) presenter;
    }
}
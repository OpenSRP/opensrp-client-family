package org.smartregister.family.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ImageView;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.domain.Photo;
import org.smartregister.util.ImageUtils;

/**
 * Created by ndegwamartin on 13/07/2018.
 */
public class ImageRenderHelper {

    private static final String TAG = org.smartregister.helper.ImageRenderHelper.class.getCanonicalName();

    private Context context;

    public ImageRenderHelper(Context context) {
        this.context = context;
    }

    public void refreshProfileImage(String clientBaseEntityId, ImageView profileImageView, int defaultProfileImage) {

        Photo photo = ImageUtils.profilePhotoByClientID(clientBaseEntityId, defaultProfileImage);

        if (StringUtils.isNotBlank(photo.getFilePath())) {
            try {
                Bitmap myBitmap = BitmapFactory.decodeFile(photo.getFilePath());
                profileImageView.setImageBitmap(myBitmap);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());

                profileImageView.setImageDrawable(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? context.getDrawable(defaultProfileImage) : ContextCompat.getDrawable(context, defaultProfileImage));

            }
        } else {
            int backgroundResource = photo.getResourceId();
            profileImageView.setImageDrawable(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? context.getDrawable(backgroundResource) : ContextCompat.getDrawable(context, backgroundResource));


        }
        profileImageView.setTag(org.smartregister.R.id.entity_id, clientBaseEntityId);
        //DrishtiApplication.getCachedImageLoaderInstance().getImageByClientId(clientBaseEntityId, OpenSRPImageLoader.getStaticImageListener(profileImageView, 0, 0));

    }
}

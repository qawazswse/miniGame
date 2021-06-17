package com.example.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import java.util.ArrayList;


public class BaseLauncher {
    private static final String TAG = "BaseLauncher";
    private final MainActivity mainActivity;
    private final ImageView launcher1;
    private final ImageView launcher2;
    private final ImageView launcher3;
    public ArrayList<ImageView> activeBase = new ArrayList<ImageView>();

    public BaseLauncher(MainActivity mainActivity, ImageView launcher1, ImageView launcher2, ImageView launcher3) {
        this.mainActivity = mainActivity;
        this.launcher1 = launcher1;
        this.launcher2 = launcher2;
        this.launcher3 = launcher3;
        setupActiveImage(launcher1,launcher2,launcher3);
    }

    private void setupActiveImage(ImageView launcher1, ImageView launcher2, ImageView launcher3) {
        activeBase.add(launcher1);
        activeBase.add(launcher2);
        activeBase.add(launcher3);
    }

    public void setBaseImageOnScreen(float x1, float y1) {

            ImageView nearestLaunchingBase = checkClosestLauncherbase(x1,y1);
            if(nearestLaunchingBase != null)
            {
                double startX = nearestLaunchingBase.getX();
                double startY = nearestLaunchingBase.getY();
                mainActivity.callInterceptor(startX,startY,x1,y1);
            }


    }

    private ImageView checkClosestLauncherbase(float x, float y) {
        float distanceForLuncherBase1 = 0.0f;
        float distanceForLuncherBase2 = 0.0f;
        float distanceForLuncherBase3 = 0.0f;

        if(activeBase.size() != 0)
        {
            for(int i = 0; i<activeBase.size();i++)
            {
                ImageView temp = activeBase.get(i);
                float launcherX = temp.getX();
                float launcherY = temp.getY();

                if(temp.getId() == launcher1.getId())
                {
                    distanceForLuncherBase1 = findDistanceFromClick(x, y, launcherX,launcherY);
                }
                if(temp.getId() == launcher2.getId())
                {
                    distanceForLuncherBase2 = findDistanceFromClick(x, y, launcherX,launcherY);
                }
                if(temp.getId() == launcher3.getId())
                {
                    distanceForLuncherBase3 = findDistanceFromClick(x, y, launcherX,launcherY);
                }

            }
            float result_distance = findMinimumFromAllBase(distanceForLuncherBase1,distanceForLuncherBase2,distanceForLuncherBase3);
            return findNearestBaseStationImage(result_distance,distanceForLuncherBase1,distanceForLuncherBase2,distanceForLuncherBase3);
        }
        else {
            return null;
        }
       
    }

    private ImageView findNearestBaseStationImage(float resultDistance, float distanceForLuncherBase1, float distanceForLuncherBase2, float distanceForLuncherBase3) {
        if(resultDistance == distanceForLuncherBase1 && distanceForLuncherBase1 != 0.0f)
        {
            return launcher1;
        }
        else if(resultDistance == distanceForLuncherBase2 && distanceForLuncherBase2 != 0.0f )
        {
            return launcher2;
        }
        else if(resultDistance == distanceForLuncherBase3 && distanceForLuncherBase3 != 0.0f)
        {
            return launcher3;
        }
        return null;
    }

    private float findMinimumFromAllBase(float distanceForLuncherBase1, float distanceForLuncherBase2, float distanceForLuncherBase3) {
        float distance;

        if(distanceForLuncherBase1 != 0.0f && distanceForLuncherBase2 != 0.0f && distanceForLuncherBase3 != 0.0f)
        {
            distance = Math.min(Math.min(distanceForLuncherBase1, distanceForLuncherBase2), distanceForLuncherBase3);
            return distance;
        }
        else if(distanceForLuncherBase1 == 0.0f && distanceForLuncherBase2 != 0.0f && distanceForLuncherBase3 != 0.0f)
        {
            distance = Math.min(distanceForLuncherBase2,distanceForLuncherBase3);
            return distance;
        }
        else if(distanceForLuncherBase2 == 0.0f && distanceForLuncherBase1 != 0.0f && distanceForLuncherBase3 != 0.0f)
        {
            distance = Math.min(distanceForLuncherBase1,distanceForLuncherBase3);
            return distance;
        }
        else if(distanceForLuncherBase3 == 0.0f && distanceForLuncherBase2 != 0.0f && distanceForLuncherBase1 != 0.0f)
        {
            distance = Math.min(distanceForLuncherBase2,distanceForLuncherBase1);
            return distance;
        }
        else if(distanceForLuncherBase1 == 0.0f && distanceForLuncherBase2 == 0.0f )
        {
            return  distanceForLuncherBase3;
        }
        else if(distanceForLuncherBase1 == 0.0f && distanceForLuncherBase3 == 0.0f)
        {
            return distanceForLuncherBase2;
        }
        else if(distanceForLuncherBase2 == 0.0f && distanceForLuncherBase3 == 0.0f)
        {
            return distanceForLuncherBase1;
        }
        return 0.0f;
    }

    private float findDistanceFromClick(float clickedX, float clickedY, float launcherX, float launcherY) {
       return (float) Math.sqrt((clickedY - launcherY) * (clickedY - launcherY) +
                (clickedX - launcherX) * (clickedX - launcherX));
    }

    public void applyMissileBlast(Missile missile) {
        float x1 = missile.getX();
        float y1 = missile.getY();
        for(int i =0 ;i <activeBase.size(); i++)
        {
            float x2 = (int) (activeBase.get(i).getX() + (0.5 * activeBase.get(i).getWidth()));
            float y2 = (int) (activeBase.get(i).getY() + (0.5 * activeBase.get(i).getHeight()));

            float f = (float) Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));

            if(f< 250)
            {
                SoundPlayer.getInstance().start("base_blast");
                baseBlastMissile(x2,y2,activeBase.get(i));
                activeBase.remove(i);
                checkBasePresent();

            }
        }
    }

    public void applyInterceptorBlast(Interceptor interceptor) {
        float x1 = interceptor.getX();
        float y1 = interceptor.getY();
        for(int i =0 ;i <activeBase.size(); i++)
        {
            float x2 = (int) (activeBase.get(i).getX() + (0.5 * activeBase.get(i).getWidth()));
            float y2 = (int) (activeBase.get(i).getY() + (0.5 * activeBase.get(i).getHeight()));

            float f = (float) Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));

            if(f< 250)
            {
                SoundPlayer.getInstance().start("base_blast");
                baseBlastMissile(x2,y2,activeBase.get(i));
                activeBase.remove(i);
                checkBasePresent();

            }
        }
    }

    private void checkBasePresent() {
        if(activeBase.size() == 0)
        {
            mainActivity.doStop();
        }
    }

    private void baseBlastMissile(float x2, float y2, final ImageView imageView) {
        imageView.setImageResource(R.drawable.blast);

        imageView.setTransitionName("Missile Intercepted Blast");

        int w = imageView.getDrawable().getIntrinsicWidth();
        int offset = (int) (w * 0.5);

        imageView.setX(x2 - offset);
        imageView.setY(y2 - offset);
        imageView.setRotation((float) (360.0 * Math.random()));

        mainActivity.getLayout().removeView(imageView);
        mainActivity.getLayout().addView(imageView);

        final ObjectAnimator alpha = ObjectAnimator.ofFloat(imageView, "alpha", 0.0f);
        alpha.setInterpolator(new LinearInterpolator());
        alpha.setDuration(3000);
        alpha.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mainActivity.getLayout().removeView(imageView);
            }


        });

        alpha.start();
    }
}

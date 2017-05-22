package splashscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.WindowManager;

import com.github.paolorotolo.appintro.AppIntro;

import jkydevelop.solidareus.Login;
import jkydevelop.solidareus.R;
import user.Preferences;


/**
 * Created by Juan Carlos on 31/12/2016.
 */

public class SplashScreen extends AppIntro {

    private Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        preferences = new Preferences(this);

        addSlide(SampleSlide.newInstance(R.layout.splash1));
        addSlide(SampleSlide.newInstance(R.layout.splash2));
        addSlide(SampleSlide.newInstance(R.layout.splash3));
        addSlide(SampleSlide.newInstance(R.layout.splash4));
        addSlide(SampleSlide.newInstance(R.layout.splash5));


        /** https://github.com/paolorotolo/AppIntro **/


        // OPTIONAL METHODS
        // Override bar/separator color.
        /*setBarColor(Color.parseColor("#3F51B5"));
        setSeparatorColor(Color.parseColor("#2196F3"));*/

        // Hide Skip/Done button.
        setProgressButtonEnabled(true);
        showSkipButton(false);
        setFadeAnimation();
    }


    @Override
    public void onBackPressed() {
        if (preferences.isSplashScreen()){
            finish();
        }
        else {
            return;
        }
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();
        // Do something when users tap on Skip button.
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        preferences.setSplashScreen(false);
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
        // Do something when users tap on Done button.
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }

}

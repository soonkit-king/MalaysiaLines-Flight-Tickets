package my.utem.ftmk.flightticketingsystem;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class PaymentValidationActivity extends AppCompatActivity {
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_validation);

        ImageView dot1 = findViewById(R.id.dot1);
        ImageView dot2 = findViewById(R.id.dot2);
        ImageView dot3 = findViewById(R.id.dot3);

        // Function to animate dot scaling smoothly
        Runnable animateDots = new Runnable() {
            int currentDot = 1;

            @Override
            public void run() {
                // Animate the current dot smoothly
                animateDot(getDotByIndex(currentDot));

                // Move to the next dot
                currentDot = (currentDot % 3) + 1;

                // Repeat every 400ms for smoother effect
                handler.postDelayed(this, 400);
            }

            private ImageView getDotByIndex(int index) {
                if (index == 1) return dot1;
                else if (index == 2) return dot2;
                else return dot3;
            }
        };

        // Start the animation loop
        handler.post(animateDots);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Navigate to BookingSuccessActivity after 3 seconds

                Intent intent = new Intent(PaymentValidationActivity.this, BookingSuccessActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); // Ensure PaymentValidationActivity is removed from the stack


            }
        }, 3000); // 3000ms = 3 seconds
    }

    private void animateDot(ImageView dot) {
        ObjectAnimator scaleUp = ObjectAnimator.ofFloat(dot, "scaleX", 1.0f, 1.5f);
        scaleUp.setDuration(400);
        scaleUp.setRepeatMode(ValueAnimator.REVERSE);
        scaleUp.setRepeatCount(1);
        scaleUp.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(dot, "scaleY", 1.0f, 1.5f);
        scaleUpY.setDuration(400);
        scaleUpY.setRepeatMode(ValueAnimator.REVERSE);
        scaleUpY.setRepeatCount(1);
        scaleUpY.setInterpolator(new AccelerateDecelerateInterpolator());

        scaleUp.start();
        scaleUpY.start();
    }
}

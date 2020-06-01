package com.romain.cellarv1.outils;

import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;


public class ProgressBarAnimation extends Animation {
    private ProgressBar progressBar;
    private int progressTo;
    private int progressFrom;
    private float rotationTo;
    private float rotationFrom;
    private long animationDuration;
    private boolean forceClockwiseRotation;
    private boolean forceCounterClockwiseRotation;

    /**
     * Default constructor
     * @param progressBar ProgressBar object
     * @param fullDuration - time required to change progress/rotation
     */
    public ProgressBarAnimation(ProgressBar progressBar, long fullDuration) {
        super();
        this.progressBar = progressBar;
        animationDuration = fullDuration;
        forceClockwiseRotation = false;
        forceCounterClockwiseRotation = false;
    }

    /**
     * Method for forcing clockwise rotation for progress bar
     * Method also disables forcing counter clockwise rotation
     * @param forceClockwiseRotation true if should force clockwise rotation for progress bar
     */
    public void forceClockwiseRotation(boolean forceClockwiseRotation) {
        this.forceClockwiseRotation = forceClockwiseRotation;

        if (forceClockwiseRotation && forceCounterClockwiseRotation) {
            // Can't force counter clockwise and clockwise rotation in the same time
            forceCounterClockwiseRotation = false;
        }
    }

    /**
     * Method for forcing counter clockwise rotation for progress bar
     * Method also disables forcing clockwise rotation
     * @param forceCounterClockwiseRotation true if should force counter clockwise rotation for progress bar
     */
    public void forceCounterClockwiseRotation(boolean forceCounterClockwiseRotation) {
        this.forceCounterClockwiseRotation = forceCounterClockwiseRotation;

        if (forceCounterClockwiseRotation && forceClockwiseRotation) {
            // Can't force counter clockwise and clockwise rotation in the same time
            forceClockwiseRotation = false;
        }
    }

    /**
     * Method for setting new progress and rotation
     * @param progress new progress
     * @param rotation new rotation
     */
    public void setProgressAndRotation(int progress, float rotation) {

        if (progressBar != null) {
            // New progress must be between 0 and max
            if (progress < 0) {
                progress = 0;
            }
            if (progress > progressBar.getMax()) {
                progress = progressBar.getMax();
            }
            progressTo = progress;

            // Rotation value should be between 0 and 360
            rotationTo = rotation % 360;

            // Current rotation value should be between 0 and 360
            if (progressBar.getRotation() < 0) {
                progressBar.setRotation(progressBar.getRotation() + 360);
            }
            progressBar.setRotation(progressBar.getRotation() % 360);

            progressFrom = progressBar.getProgress();
            rotationFrom = progressBar.getRotation();

            // Check for clockwise rotation
            if (forceClockwiseRotation && rotationTo < rotationFrom) {
                rotationTo += 360;
            }

            // Check for counter clockwise rotation
            if (forceCounterClockwiseRotation && rotationTo > rotationFrom) {
                rotationTo -= 360;
            }

            setDuration(animationDuration);
            progressBar.startAnimation(this);
        }
    }

    /**
     * Method for setting only progress for progress bar
     * @param progress new progress
     */
    public void setProgressOnly(int progress) {
        if (progressBar != null) {
            setProgressAndRotation(progress, progressBar.getRotation());
        }
    }

    /**
     * Method for setting only rotation for progress bar
     * @param rotation new rotation
     */
    public void setRotationOnly(float rotation) {
        if (progressBar != null) {
            setProgressAndRotation(progressBar.getProgress(), rotation);
        }
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        float progress = progressFrom + (progressTo - progressFrom) * interpolatedTime;
        float rotation = rotationFrom + (rotationTo - rotationFrom) * interpolatedTime;

        // Set new progress and rotation
        if (progressBar != null) {
            progressBar.setProgress((int) progress);
            progressBar.setRotation(rotation);
        }
    }
}

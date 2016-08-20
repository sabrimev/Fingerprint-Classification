/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerprintrecognition;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.RenderedImage;

public class GaborFilter {

    private final double standardDeviation;
    private final double orientation;
    private final double waveLength;
    private final double phaseOffset;
    private final double aspectRatio;

    private int[][] x, y;
    private int width, height;
    private final ConvolveOp convolveOp;

    public GaborFilter(double standardDeviation, double orientation,
            double waveLength, double phaseOffset, double aspectRatio) {
        this.standardDeviation = standardDeviation;
        this.orientation = orientation;
        this.waveLength = waveLength;
        this.phaseOffset = phaseOffset;
        this.aspectRatio = aspectRatio;
        this.convolveOp = new ConvolveOp(createKernel(), ConvolveOp.EDGE_ZERO_FILL, null);
    }

    private Kernel createKernel() {
        double sigmaX = standardDeviation;
        double sigmaY = standardDeviation / aspectRatio;

        int nstds = 3;
        int xMax = (int) Math.max(Math.abs(nstds * sigmaX * Math.cos(orientation)),
                Math.abs(nstds * sigmaY * Math.sin(orientation)));
        xMax = (int) Math.ceil(Math.max(1, xMax));
        int yMax = (int) Math.max(Math.abs(nstds * sigmaX * Math.sin(orientation)),
                Math.abs(nstds * sigmaY * Math.cos(orientation)));
        yMax = (int) Math.ceil(Math.max(1, yMax));
        int xMin = -xMax;
        int yMin = -yMax;
        meshgrid(xMin, xMax, yMin, yMax);

        float[] data = new float[width * height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                double xTheta = x[j][i] * Math.cos(orientation)
                        + y[j][i] * Math.sin(orientation);
                double yTheta = -x[j][i] * Math.sin(orientation)
                        + y[j][i] * Math.cos(orientation);
                data[j * i + i] = (float) (Math.exp(-0.5
                        * (Math.pow(xTheta, 2) / Math.pow(sigmaX, 2)
                        + Math.pow(yTheta, 2) / Math.pow(sigmaY, 2)))
                        * Math.cos(2 * Math.PI / waveLength * xTheta + phaseOffset));
            }
        }
        return new Kernel(width, height, data);
    }

    private void meshgrid(int xMin, int xMax, int yMin, int yMax) {
        width = xMax - xMin + 1;
        height = yMax - yMin + 1;

        x = new int[width][height];
        y = new int[width][height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                x[j][i] = j + xMin;
                y[j][i] = i + yMin;
            }
        }
    }

    public RenderedImage filter(BufferedImage originalImage, BufferedImage filteredImage) {
        return convolveOp.filter(originalImage, filteredImage);
    }
}
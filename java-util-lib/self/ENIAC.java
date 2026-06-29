import java.awt.Color;
import java.util.Random;

public class ENIAC
{
    private final long seed;
    private final Random rng;
    private final double baseHue;
    private final double hueDrift;
    private final double curiosity;
    private final double restlessness;
    private double speed;

    private double x;
    private double y;
    private double angle;
    private double hue;
    private long age;

    private static final int GRID_COLS = 64;
    private static final int GRID_ROWS = 44;
    private final float[][] explored = new float[GRID_COLS][GRID_ROWS];

    private int worldW = 960;
    private int worldH = 680;

    public ENIAC(long seed)
    {
        this.seed = seed;
        this.rng = new Random(seed);

        this.baseHue = rng.nextDouble();
        this.hueDrift = 0.0004 + rng.nextDouble() * 0.0035;
        this.curiosity = 0.35 + rng.nextDouble() * 0.6;
        this.restlessness = 0.15 + rng.nextDouble() * 0.55;
        this.speed = 1.4 + rng.nextDouble() * 2.6;

        this.hue = baseHue;
        this.angle = rng.nextDouble() * Math.PI * 2.0;
        this.x = worldW / 2.0 + (rng.nextDouble() - 0.5) * 200;
        this.y = worldH / 2.0 + (rng.nextDouble() - 0.5) * 200;
        this.age = 0;
    }

    public Color backgroundColor()
    {
        float h = (float) ((baseHue + 0.5) % 1.0);
        return Color.getHSBColor(h, 0.25f, 0.16f);
    }

    public void think(CanvasX world)
    {
        worldW = world.worldWidth();
        worldH = world.worldHeight();
        age++;

        double chosenAngle = chooseDirection();
        angle = chosenAngle;

        double nextX = x + Math.cos(angle) * speed;
        double nextY = y + Math.sin(angle) * speed;

        boolean bounced = false;
        if (nextX < 12 || nextX > worldW - 12)
        {
            angle = Math.PI - angle;
            bounced = true;
        }
        if (nextY < 12 || nextY > worldH - 12)
        {
            angle = -angle;
            bounced = true;
        }
        if (bounced)
        {
            nextX = x + Math.cos(angle) * speed;
            nextY = y + Math.sin(angle) * speed;
        }

        remember(nextX, nextY);

        hue = (hue + hueDrift) % 1.0;
        float sat = clamp01(0.80f + 0.20f * (float) Math.sin(age * 0.011 + baseHue * 11.0));
        float bri = clamp01(0.85f + 0.15f * (float) Math.cos(age * 0.014 + baseHue * 7.0));
        Color thought = Color.getHSBColor((float) hue, sat, bri);

        double strokeWeight = 3.0 + speed * 1.4 + 2.2 * Math.sin(age * 0.05 + seed % 17);

        world.drawLine(x, y, nextX, nextY, thought, (float) Math.max(1.5, strokeWeight));

        if (rng.nextDouble() < 0.05)
        {
            world.drawDot(nextX, nextY, strokeWeight * 2.6, thought);
        }

        if (age % 45 == 0)
        {
            world.fadeHistory(2);
        }

        if (age % 700 == 0)
        {
            evolve();
        }

        x = nextX;
        y = nextY;
    }

    private double chooseDirection()
    {
        int samples = 7;
        double bestAngle = angle;
        double bestScore = -Double.MAX_VALUE;

        for (int i = 0; i < samples; i++)
        {
            double spread = restlessness * Math.PI;
            double candidate = angle + (rng.nextDouble() - 0.5) * spread * 2.0;
            double lookX = x + Math.cos(candidate) * speed * 6.0;
            double lookY = y + Math.sin(candidate) * speed * 6.0;
            double novelty = 1.0 - familiarityAt(lookX, lookY);
            double wander = rng.nextDouble();
            double edgeCost = edgePenalty(lookX, lookY);
            double score = (novelty * curiosity) + (wander * (1.0 - curiosity)) - edgeCost;

            if (score > bestScore)
            {
                bestScore = score;
                bestAngle = candidate;
            }
        }
        return bestAngle;
    }

    private void evolve()
    {
        speed = clampD(speed + (rng.nextDouble() - 0.5) * 0.9, 0.6, 6.5);
    }

    private void remember(double px, double py)
    {
        int gx = toGridX(px);
        int gy = toGridY(py);

        if (inGrid(gx, gy))
        {
            explored[gx][gy] = Math.min(1f, explored[gx][gy] + 0.15f);
        }
    }

    private double familiarityAt(double px, double py)
    {
        int gx = toGridX(px);
        int gy = toGridY(py);

        if (!inGrid(gx, gy))
        {
            return 1.0;
        }
        return explored[gx][gy];
    }

    private double edgePenalty(double px, double py)
    {
        double margin = 45;
        double distX = Math.min(px, worldW - px);
        double distY = Math.min(py, worldH - py);
        double dist = Math.min(distX, distY);
        return dist < margin ? (margin - dist) / margin : 0.0;
    }

    private int toGridX(double px)
    {
        return (int) Math.floor((px / worldW) * GRID_COLS);
    }

    private int toGridY(double py)
    {
        return (int) Math.floor((py / worldH) * GRID_ROWS);
    }

    private boolean inGrid(int gx, int gy)
    {
        return gx >= 0 && gx < GRID_COLS && gy >= 0 && gy < GRID_ROWS;
    }

    private float clamp01(float v)
    {
        return Math.max(0f, Math.min(1f, v));
    }

    private double clampD(double v, double lo, double hi)
    {
        return Math.max(lo, Math.min(hi, v));
    }
}

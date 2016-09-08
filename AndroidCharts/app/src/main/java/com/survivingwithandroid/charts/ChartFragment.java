package com.survivingwithandroid.charts;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.survivingwithandroid.weather.lib.WeatherClient;
import com.survivingwithandroid.weather.lib.WeatherConfig;
import com.survivingwithandroid.weather.lib.client.volley.WeatherClientDefault;
import com.survivingwithandroid.weather.lib.exception.WeatherLibException;
import com.survivingwithandroid.weather.lib.model.DayForecast;
import com.survivingwithandroid.weather.lib.model.HourForecast;
import com.survivingwithandroid.weather.lib.model.WeatherForecast;
import com.survivingwithandroid.weather.lib.model.WeatherHourForecast;
import com.survivingwithandroid.weather.lib.provider.IWeatherProvider;
import com.survivingwithandroid.weather.lib.provider.WeatherProviderFactory;
import com.survivingwithandroid.weather.lib.provider.openweathermap.OpenweathermapProviderType;


import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.RangeCategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * ${copyright}.
 */
public class ChartFragment extends Fragment {

    private LinearLayout chartLyt;
    private Animation fadeAnim;

    // London, UK
    private static final String CITY_ID = "2643743";

    private List<DayForecast> dayForecast;
    private List<HourForecast> nextHourForecast;

    /**
     * Called when the fragment's activity has been created and this
     * fragment's view hierarchy instantiated.  It can be used to do final
     * initialization once these pieces are in place, such as retrieving
     * views or restoring state.  It is also useful for fragments that use
     * {@link #setRetainInstance(boolean)} to retain their instance,
     * as this callback tells the fragment when it is fully associated with
     * the new activity instance.  This is called after {@link #onCreateView}
     * and before {@link #onViewStateRestored(android.os.Bundle)}.
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fadeAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_anim);
        getData();
    }


    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation).  This will be called between
     * {@link #onCreate(android.os.Bundle)} and {@link #onActivityCreated(android.os.Bundle)}.
     * <p/>
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        chartLyt = (LinearLayout) v.findViewById(R.id.chart);
        return v;
    }

    private void getData() {
        WeatherClient client = setupClient(getActivity());
        client.getHourForecastWeather(CITY_ID, new WeatherClient.HourForecastWeatherEventListener() {
            @Override
            public void onWeatherRetrieved(WeatherHourForecast weatherHourForecast) {
                nextHourForecast = weatherHourForecast.getHourForecast();
                Toast.makeText(getActivity(), "Data retrieved", Toast.LENGTH_LONG).show();
                chartLyt.addView(createTempGraph(), 0);
           }

            @Override
            public void onWeatherError(WeatherLibException e) {
                Toast.makeText(getActivity(), "Error parsing the data", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onConnectionError(Throwable throwable) {
                Toast.makeText(getActivity(), "Error connecting to the remove weather server!", Toast.LENGTH_LONG).show();
            }
        });

        client.getForecastWeather(CITY_ID, new WeatherClient.ForecastWeatherEventListener() {
            @Override
            public void onWeatherRetrieved(WeatherForecast weatherForecast) {
                dayForecast = weatherForecast.getForecast();
                Toast.makeText(getActivity(), "Forecast retrieved!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onWeatherError(WeatherLibException e) {
                Log.e("SwA", "#####à ERROR #####", e);
                Toast.makeText(getActivity(), "Forecast: Error parsing the data", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onConnectionError(Throwable throwable) {
                Toast.makeText(getActivity(), "Forecast: Error connecting to the remove weather server!", Toast.LENGTH_LONG).show();
            }
        });
    }


    private WeatherClient setupClient(Context ctx) {
        WeatherClient client = WeatherClientDefault.getInstance();
        client.init(ctx);

        WeatherConfig config = new WeatherConfig();
        config.unitSystem = WeatherConfig.UNIT_SYSTEM.M;
        config.numDays = 6;
        try {
            IWeatherProvider provider = WeatherProviderFactory.createProvider(new OpenweathermapProviderType(), config);
            client.setProvider(provider);
        }
        catch (Throwable t) {
            t.printStackTrace();
            // There's a problem
        }

        return client;
    }

    private View createTempGraph() {
        // We start creating the XYSeries to plot the temperature
        XYSeries series = new XYSeries("London Temperature hourly");

        // We start filling the series
        int hour = 0;
        for (HourForecast hf : nextHourForecast) {
            series.add(hour++, hf.weather.temperature.getTemp());
        }

        // Now we create the renderer
        XYSeriesRenderer renderer = new XYSeriesRenderer();
        renderer.setLineWidth(2);
        renderer.setColor(Color.RED);
        // Include low and max value
        renderer.setDisplayBoundingPoints(true);
        // we add point markers
        renderer.setPointStyle(PointStyle.CIRCLE);
        renderer.setPointStrokeWidth(3);


        // Now we add our series
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(series);

        // Finaly we create the multiple series renderer to control the graph
        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
        mRenderer.addSeriesRenderer(renderer);

        // We want to avoid black border
        mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00)); // transparent margins
        // Disable Pan on two axis
        mRenderer.setPanEnabled(false, false);
        mRenderer.setYAxisMax(35);
        mRenderer.setYAxisMin(0);
        mRenderer.setShowGrid(true); // we show the grid
        GraphicalView chartView = ChartFactory.getLineChartView(getActivity(), dataset, mRenderer);


        // Enable chart click
        mRenderer.setClickEnabled(true);
        chartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              applyAnim(v, createPressGraph());
            }
        });

        return chartView;
    }

    private View createPressGraph() {
        XYSeries series = new XYSeries("London Pressure hourly");

        // We start filling the series
        int hour = 0;

        for (HourForecast hf : nextHourForecast) {
            series.add(hour++, hf.weather.currentCondition.getPressure());

            if (hour > 24)
                break;
        }

        // Now we create the renderer
        XYSeriesRenderer renderer = new XYSeriesRenderer();
        renderer.setColor(Color.BLUE);
        // Include low and max value
        renderer.setDisplayBoundingPoints(true);

        // Now we add our series
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(series);

        // Finaly we create the multiple series renderer to control the graph
        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
        mRenderer.addSeriesRenderer(renderer);

        // We want to avoid black border
        mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00)); // transparent margins
        // Disable Pan on two axis
        mRenderer.setPanEnabled(false, false);

        mRenderer.setShowGrid(true); // we show the grid
        mRenderer.setBarSpacing(1);

        GraphicalView chartView = ChartFactory.getBarChartView(getActivity(), dataset, mRenderer, BarChart.Type.DEFAULT);

        // Enable chart click
        mRenderer.setClickEnabled(true);
        chartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyAnim(v, createForecastTemp());
            }
        });
        return chartView;
    }

    private View createForecastTemp() {
        RangeCategorySeries series = new RangeCategorySeries("London next days temperature");

        // We start filling the series
        int hour = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd,MMM");

        // We create the multiple series renderer to control the graph
        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

        for (DayForecast df : dayForecast) {
            series.add(df.forecastTemp.min, df.forecastTemp.max);
            mRenderer.addXTextLabel(hour++, sdf.format(df.timestamp));
        }



        //renderer.setGradientStop(20, Color.RED);

        // Now we add our series
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(series.toXYSeries());






        // We want to avoid black border
        mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00)); // transparent margins
        // Disable Pan on two axis
        mRenderer.setPanEnabled(false, false);

        mRenderer.setShowGrid(true); // we show the grid
        mRenderer.setBarSpacing(0.5);
        mRenderer.setMargins(new int[] {30, 70, 10, 0});
        XYSeriesRenderer renderer = new XYSeriesRenderer();

        renderer.setDisplayChartValues(true);
        mRenderer.addSeriesRenderer(renderer);

        mRenderer.setYAxisMax(30.0);
        mRenderer.setYAxisMin(0.0);

        renderer.setChartValuesTextSize(12);
        renderer.setChartValuesFormat(new DecimalFormat("#.##"));
        renderer.setColor(Color.GREEN);
        GraphicalView chartView = ChartFactory.getRangeBarChartView(getActivity(), dataset, mRenderer, BarChart.Type.DEFAULT);

        // Enable chart click
        mRenderer.setClickEnabled(true);
        chartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyAnim(v, createTempGraph());
            }
        });
        return chartView;
    }

    private void applyAnim(final View v, final View nextView) {

        Animation.AnimationListener list = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                chartLyt.removeViewAt(0);
                chartLyt.addView(nextView,0);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };
        fadeAnim.setAnimationListener(list);
        v.startAnimation(fadeAnim);
    }
}

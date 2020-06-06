package claudiu.sics.smsfilter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import sisc.claudiu.smsfilter.R;

public class FilterDateActivity extends AppCompatActivity {

    private Filter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_date_filter);
        final Intent intent = getIntent();
        if (intent != null) {
            filter = (Filter) intent.getSerializableExtra(Resources.EDIT_FILTER);
        }
        if (filter == null) {
            filter = new Filter();
            filter.setDateFilter(true);
        } else {
            ((EditText) findViewById(R.id.editLabelDateText)).setText(filter.getLabel());
            ((EditText) findViewById(R.id.editPhoneNumberDateText)).setText(filter.getPhonePattern());
            ((EditText) findViewById(R.id.editMessagePatternDateText)).setText(filter.getMessagePattern());
            ((EditText) findViewById(R.id.fromDateText)).setText(filter.getStartTime());
            ((EditText) findViewById(R.id.toDateText)).setText(filter.getEndTime());
        }

        Log.d(getClass().getSimpleName(), "Editing DATE filter: " + filter);
    }

    public Filter getFilter() {
        return filter;
    }

    public void pickFrom(View view) {
        /*
        陈礼轩 2020-6-6 生成签名APK 失败，所以不用匿名类
        DialogFragment newFragment = new DatePickerFragment() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                super.onDismiss(dialog);
                ((EditText) findViewById(R.id.fromDateText)).setText(filter.getStartTime());
            }
        };
        newFragment.show(getFragmentManager(), Resources.DATE_PICKER_FROM);
        */

        MyDatePickerFragment newFragment =new MyDatePickerFragment();
        newFragment.setFilter(filter.getStartTime(),((EditText) findViewById(R.id.fromDateText)));

        newFragment.show(getFragmentManager(), Resources.DATE_PICKER_FROM);
    }



    public void pickTo(View view) {

        /*
        陈礼轩 2020-6-6 生成签名APK 失败，所以不用匿名类
        DialogFragment newFragment = new DatePickerFragment() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                super.onDismiss(dialog);
                ((EditText) findViewById(R.id.toDateText)).setText(filter.getEndTime());
            }
        };

        newFragment.setCancelable(true);
        newFragment.show(getFragmentManager(), Resources.DATE_PICKER_TO);

        */

        MyDatePickerFragment newFragment =new MyDatePickerFragment();
        newFragment.setFilter(filter.getEndTime(),((EditText) findViewById(R.id.toDateText)));
        newFragment.setCancelable(true);
        newFragment.show(getFragmentManager(), Resources.DATE_PICKER_TO);
    }

    public void saveDateFilter(View view) {
        EditText labelText = (EditText) findViewById(R.id.editLabelDateText);
        EditText phoneText = (EditText) findViewById(R.id.editPhoneNumberDateText);
        EditText patternText = (EditText) findViewById(R.id.editMessagePatternDateText);
        String label = labelText.getText().toString();
        String pattern = patternText.getText().toString();
        String phone = phoneText.getText().toString();
        filter.setLabel(label);
        filter.setMessagePattern(pattern);
        filter.setDateFilter(true);
        filter.setPhonePattern(phone);
        final Intent result = new Intent();
        result.putExtra(Resources.EDIT_FILTER, filter);
        setResult(Activity.RESULT_OK, result);
        finish();
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Filter filter = ((FilterDateActivity) getActivity()).getFilter();
            final String tag = getTag();
            String editValue = null;
            if (Resources.DATE_PICKER_FROM.equals(tag)) {
                editValue = filter.getStartTime();
            } else if (Resources.DATE_PICKER_TO.equals(tag)) {
                editValue = filter.getEndTime();
            }
            Calendar calendar = Calendar.getInstance();
            if (editValue != null) {
                try {
                    final Date parse = new SimpleDateFormat(Resources.FORMAT_YYYY_MM_DD).parse(editValue);
                    calendar.setTime(parse);
                } catch (ParseException e) {
                    Log.e(FilterDateActivity.class.getSimpleName(), "Error parsing date", e);
                }
            }
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            final Filter filter = ((FilterDateActivity) getActivity()).getFilter();
            final String tag = getTag();
            if (Resources.DATE_PICKER_FROM.equals(tag)) {
                final Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                filter.setStartTime(DateFormat.format(Resources.FORMAT_YYYY_MM_DD, calendar.getTime()).toString());
            } else if (Resources.DATE_PICKER_TO.equals(tag)) {
                final Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                filter.setEndTime(DateFormat.format(Resources.FORMAT_YYYY_MM_DD, calendar.getTime()).toString());
            }
        }
    }
}


@SuppressLint("ValidFragment")
class MyDatePickerFragment extends FilterDateActivity.DatePickerFragment {

    private String stringTime;
    private EditText editText;

    public void setFilter( String stringTime, EditText editText){
        this.stringTime=stringTime;
        this.editText=editText;
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        this.editText.setText(stringTime);
    }
}
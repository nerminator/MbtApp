package com.daimlertruck.dtag.internal.android.mbt.test.ui.main.orchestra.schedule;

import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import androidx.recyclerview.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimlertruck.dtag.internal.android.mbt.test.R;
import com.daimlertruck.dtag.internal.android.mbt.test.adapters.WorkCalendarCauseAdapter;
import com.daimlertruck.dtag.internal.android.mbt.test.adapters.WorkingScheduleAdapter;
import com.daimlertruck.dtag.internal.android.mbt.test.base.BaseFragment;
import com.daimlertruck.dtag.internal.android.mbt.test.databinding.FragmentWorkingScheduleBinding;
import com.daimlertruck.dtag.internal.android.mbt.test.model.Day;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.workCalendar.DayEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.workCalendar.TypeEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.network.entity.workCalendar.WorkCalendarEntity;
import com.daimlertruck.dtag.internal.android.mbt.test.ui.toolbar.VMToolbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkingScheduleFragment extends BaseFragment {

    private FragmentWorkingScheduleBinding binding;
    private int year;
    private int month;
    private int day;
    private List<Day> days = new ArrayList<>();
    private WorkingScheduleAdapter adapter;
    private VMWorkingScheduleFragment vmWorkingSchedule;
    private boolean monthIsCurrentMonth = false;
    private Calendar calendar = Calendar.getInstance();
    private WorkCalendarCauseAdapter workCalendarCauseAdapter;
    private Day dayForAdapter = new Day();
    private Handler handler = new Handler();
    private Handler handlerPaging = new Handler();
    private String headerMonthName = "";
    private List<TypeEntity> typeEntityList=new ArrayList<>();

    public WorkingScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_working_schedule, container, false);
        VMToolbar vmToolbar = VMToolbar.createVMToolbarFor(this, getString(R.string.TXT_PROFILE_PROFILE_2ND_BUTTON), null);
        vmWorkingSchedule = ViewModelProviders.of(this).get(VMWorkingScheduleFragment.class);
        binding.toolbar.setVm(vmToolbar);
        binding.setViewmodel(vmWorkingSchedule);
        init();
        return binding.getRoot();
    }

    private void init() {
        setRecycler();
        observe();
        getPageDateMetrics();
    }

    private void setRecycler() {
        IWorkCalendar iWorkCalendar = day -> {
            for (int i = 0; i < days.size(); i++) {
                Day currentDay=days.get(i);
                currentDay.setSelected(false);
            }
            day.setSelected(true);
            dayForAdapter.setObject(day);
            for (int i = 0; i < dayForAdapter.getTypeEntityList().size(); i++) {
                dayForAdapter.getTypeEntityList().get(i).setDayText(dayForAdapter.getDayText());
            }
            typeEntityList.clear();
            typeEntityList.addAll(dayForAdapter.getTypeEntityList());
            workCalendarCauseAdapter.notifyDataSetChanged();
        };
        adapter = new WorkingScheduleAdapter(days, iWorkCalendar);
        workCalendarCauseAdapter = new WorkCalendarCauseAdapter(dayForAdapter, typeEntityList);
        binding.recyclerCause.setAdapter(workCalendarCauseAdapter);
        binding.recyclerCalendar.setLayoutManager(new GridLayoutManager(getContext(), 7));
        binding.recyclerCalendar.setAdapter(adapter);
    }

    private void observe() {
        vmWorkingSchedule.isLeftButtonClicked.observe(getViewLifecycleOwner(), (Boolean bool) -> {
            if (bool != null && bool) decreaseMonth();
        });
        vmWorkingSchedule.isRightButtonClicked.observe(getViewLifecycleOwner(), (Boolean bool) -> {
            if (bool != null && bool) increaseMonth();
        });

        vmWorkingSchedule.getWorkCalendarData().observe(getViewLifecycleOwner(), this::updateCalendar);
    }

    private void increaseMonth() {
        if (month == 11) {
            month = 0;
            year++;
        } else month++;

        resetPage();
    }

    private void resetPage() {
        binding.recyclerCalendar.removeAllViews();
        vmWorkingSchedule.cancelRequest();
        handler.removeCallbacksAndMessages(null);
        handlerPaging.removeCallbacksAndMessages(null);
        binding.nestedWorkCalendar.post(new Runnable() {
            @Override
            public void run() {
                binding.nestedWorkCalendar.scrollTo(0, 0);
            }
        });

        fillDayList();
        updateCalendar();
        sendDataRequest();
    }

    private void decreaseMonth() {
        if (month == 0) {
            month = 11;
            year--;
        } else month--;


        resetPage();
    }

    private void sendDataRequest() {
        handlerPaging.postDelayed(() -> {
            vmWorkingSchedule.getWorkCalendar(formatDateForApiCall(month + 1, year));
        }, 700);
    }

    private String formatDateForApiCall(int month, int year) {
        String smonth = (month < 10) ? "0" + month : String.valueOf(month);
        return "01." + smonth + "." + year;
    }

    private void updateCalendar() {

        setMonthName();
        adapter.notifyDataSetChanged();
        binding.recyclerCalendar.scheduleLayoutAnimation();
    }

    private void updateCalendar(WorkCalendarEntity workCalendarData) {
        addFeatureToDays(days, workCalendarData);
        dayForAdapter=new Day();
        typeEntityList.clear();
        typeEntityList.addAll(workCalendarData.getInitializeDayEntityList());
        workCalendarCauseAdapter.notifyDataSetChanged();
        adapter.notifyDataSetChanged();
    }

    private void setMonthName() {
        switch (month) {
            case 0:
                headerMonthName = getString(R.string.TXT_COMMON_JANUARY);
                break;
            case 1:
                headerMonthName = getString(R.string.TXT_COMMON_FEBRUARY);
                break;
            case 2:
                headerMonthName = getString(R.string.TXT_COMMON_MARCH);
                break;
            case 3:
                headerMonthName = getString(R.string.TXT_COMMON_APRIL);
                break;
            case 4:
                headerMonthName = getString(R.string.TXT_COMMON_MAY);
                break;
            case 5:
                headerMonthName = getString(R.string.TXT_COMMON_JUNE);
                break;
            case 6:
                headerMonthName = getString(R.string.TXT_COMMON_JULY);
                break;
            case 7:
                headerMonthName = getString(R.string.TXT_COMMON_AUGUST);
                break;
            case 8:
                headerMonthName = getString(R.string.TXT_COMMON_SEPTEMBER);
                break;
            case 9:
                headerMonthName = getString(R.string.TXT_COMMON_OCTOBER);
                break;
            case 10:
                headerMonthName = getString(R.string.TXT_COMMON_NOVEMBER);
                break;
            case 11:
                headerMonthName = getString(R.string.TXT_COMMON_DECEMBER);
                break;
        }
        binding.tvMonthAndYear.post(() -> binding.tvMonthAndYear.setText(headerMonthName + " " + year));
    }

    private void getPageDateMetrics() {
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        monthIsCurrentMonth = true;
        fillDayList();
        setMonthName();

        handler.postDelayed(() -> {
            vmWorkingSchedule.getWorkCalendar(getSimpleFormat().format(calendar.getTime()));
        }, 700);
    }

    private SimpleDateFormat getSimpleFormat() {
        return new SimpleDateFormat("dd.MM.yyyy", Locale.US);
    }

    private void fillDayList() {
        monthIsCurrentMonth = year == calendar.get(Calendar.YEAR) && month == calendar.get(Calendar.MONTH);
        days.clear();
        if (adapter != null)
            adapter.notifyDataSetChanged();
        binding.recyclerCalendar.invalidate();

        Calendar calForMonth = new GregorianCalendar(year, month, 1);
        //Pazartesi ilk gün olabilmesi için 1 önce başlattık
        Calendar calForWeek = new GregorianCalendar(year, month, 0);
        int daysInMonth = calForMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
        int startOfCalendarCount = calForWeek.get(Calendar.DAY_OF_WEEK);

        int previousMonthDaysCount = getPreviousMonthDaysCount(year, month);

        if (startOfCalendarCount == 0) {
            startOfCalendarCount = 7;
        } else startOfCalendarCount--;

        Integer nextMonthIndex, nextMonthYearIndex, previousMonthIndex, previousMonthYearIndex;
        if (month == 11) {
            nextMonthIndex = 0;
            nextMonthYearIndex = year + 1;
        } else {
            nextMonthIndex = month + 1;
            nextMonthYearIndex = year;
        }

        if (month == 0) {
            previousMonthIndex = 11;
            previousMonthYearIndex = year - 1;
        } else {
            previousMonthIndex = month - 1;
            previousMonthYearIndex = year;
        }


        for (int i = 0; i < startOfCalendarCount; i++) {
            //önceki ay
            Day mDay = new Day(String.valueOf(previousMonthDaysCount - startOfCalendarCount + i + 1), false);
            Calendar dateCalendar = new GregorianCalendar(previousMonthYearIndex, previousMonthIndex, previousMonthDaysCount - startOfCalendarCount + i + 1);
            mDay.setDateText(getSimpleFormat().format(dateCalendar.getTime()));
            mDay.setTypeEntityList(new ArrayList<>());
            days.add(mDay);
        }

        for (int i = 0; i < daysInMonth; i++) {
            //şimdiki ay
            boolean isToday = monthIsCurrentMonth && (day == (i + 1));
            Day mDay = new Day(String.valueOf(i + 1), true, isToday);
            Calendar dateCalendar = new GregorianCalendar(year, month, i + 1);
            mDay.setDateText(getSimpleFormat().format(dateCalendar.getTime()));
            mDay.setTypeEntityList(new ArrayList<>());
            days.add(mDay);
        }

        int endOfCalenderCount = 0;
        int daysModSeven = days.size() % 7;
        if (daysModSeven != 0) {
            endOfCalenderCount = 7 - daysModSeven;
        }
        for (int i = 0; i < endOfCalenderCount; i++) {
            //sonraki ay
            Day mDay = new Day(String.valueOf(i + 1), false);
            Calendar dateCalendar = new GregorianCalendar(nextMonthYearIndex, nextMonthIndex, i + 1);
            mDay.setDateText(getSimpleFormat().format(dateCalendar.getTime()));
            mDay.setTypeEntityList(new ArrayList<>());
            days.add(mDay);
        }
    }

    private void addFeatureToDays(List<Day> days, WorkCalendarEntity workCalendarEntity) {
        for (int i = 0; i < workCalendarEntity.getDayList().size(); i++) {
            DayEntity dayEntity = workCalendarEntity.getDayList().get(i);
            for (int j = 0; j < days.size(); j++) {
                Day cDay = days.get(j);
                if (dayEntity.getDay().equals(cDay.getDateText())) {
                    cDay.setTypeEntityList(dayEntity.getTypeList());
                    cDay.setDayText(dayEntity.getDayText());
                }
            }
        }
    }

    private int getPreviousMonthDaysCount(int year, int month) {
        if (month == 0) {
            month = 11;
            year--;
        } else month--;
        Calendar calForMonth = new GregorianCalendar(year, month, 1);
        return calForMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static WorkingScheduleFragment newInstance() {
        Bundle args = new Bundle();
        WorkingScheduleFragment fragment = new WorkingScheduleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public interface IWorkCalendar {
        void onItemClicked(Day day);
    }

}

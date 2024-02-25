package com.rd.t1d.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rd.t1d.data.entity.node.User;
import com.rd.t1d.data.entity.projection.AccessoryResult;
import com.rd.t1d.data.entity.projection.BgReadingResult;
import com.rd.t1d.data.entity.projection.ExerciseResult;
import com.rd.t1d.data.entity.projection.InsulinDoseResult;
import com.rd.t1d.data.entity.relations.BasalRate;
import com.rd.t1d.enums.InsulinDoseType;
import lombok.Data;
import org.apache.commons.math3.util.Precision;

import java.text.DecimalFormat;
import java.util.List;

@Data
public class UserLogDetails {

    @JsonProperty("bg_log")
    private List<BgReadingResult> bgReadingList;

    @JsonProperty("insulin_log")
    private List<InsulinDoseResult> insulinDoseList;

    @JsonProperty("exercise_log")
    private List<ExerciseResult> exerciseList;

    @JsonProperty("accessory_log")
    private List<AccessoryResult> accessoryList;

    @JsonProperty("basal_rate_log")
    private List<BasalRate> basalRateList;

    @JsonProperty("bg_stat")
    private BGStat bgStat;

    @JsonProperty("insulin_stat")
    private InsulinStat insulinStat;

    @JsonProperty("exercise_stat")
    private ExerciseStat exerciseStat;

    @JsonProperty("basal_rate_stat")
    private BasalRateStat basalRateStat;
    
    public void generateStats(User user){
        bgStat = new BGStat();
        bgStat.generateBgStat(user, bgReadingList);
        
        insulinStat = new InsulinStat();
        insulinStat.generateInsulinStat(insulinDoseList);
        
        exerciseStat = new ExerciseStat();
        exerciseStat.generateExerciseStat(exerciseList);

        basalRateStat = new BasalRateStat();
        basalRateStat.generateBasalRateStat(basalRateList);
    }
}


@Data
class BGStat{

    @JsonProperty("reading_count")
    private int readingCount;

    @JsonProperty("average_reading")
    private double averageReading;

    @JsonProperty("percentage_in_range")
    private double percentageInRange;

    public void generateBgStat(User user, List<BgReadingResult> bgReadingList) {

        if(bgReadingList != null && bgReadingList.size() > 0){

            bgReadingList.forEach(bgReading -> {
                readingCount++;
                averageReading += bgReading.getBgNumber();
                if(bgReading.getBgNumber() >= user.getNormalBgMin() && bgReading.getBgNumber() <= user.getNormalBgMax()){
                    percentageInRange++;
                }
            });

            averageReading = Precision.round(averageReading/bgReadingList.size(), 2);

            percentageInRange = Precision.round((percentageInRange * 100)/bgReadingList.size(), 2);
        }
    }
}

@Data
class InsulinStat{

    @JsonProperty("total_bolus")
    private double totalBolus;

    @JsonProperty("total_basal")
    private double totalBasal;

    public void generateInsulinStat(List<InsulinDoseResult> insulinDoseList) {

        if(insulinDoseList != null && insulinDoseList.size() > 0){
            insulinDoseList.forEach(insulinDose -> {
                if(InsulinDoseType.BOLUS.equals(insulinDose.getInsulinDoseType())) totalBolus += insulinDose.getQuantity();
                else if(InsulinDoseType.BASAL.equals(insulinDose.getInsulinDoseType())) totalBasal+= insulinDose.getQuantity();
            });

            totalBolus = Precision.round(totalBolus, 2);
            totalBasal = Precision.round(totalBasal, 2);
        }
    }
}

@Data
class ExerciseStat{

    @JsonProperty("duration_in_minutes")
    private int durationInMinutes;

    public void generateExerciseStat(List<ExerciseResult> exerciseList) {

        if(exerciseList != null && exerciseList.size() > 0){
            exerciseList.forEach(exercise -> {
                durationInMinutes += exercise.getDurationInMinutes();
            });
        }
    }
}

@Data
class BasalRateStat{

    @JsonIgnore
    private double totalQuantity;

    @JsonProperty("total_pump_basal")
    private String totalPumpBasal;

    public void generateBasalRateStat(List<BasalRate> basalRateList) {

        if(basalRateList != null && basalRateList.size() > 0){
            basalRateList.forEach(basalRate -> {
                totalQuantity += (2*basalRate.getInsulinDose().getQuantity());
            });
            DecimalFormat df = new DecimalFormat("0.00");
            totalPumpBasal = df.format(totalQuantity);
        }
    }
}

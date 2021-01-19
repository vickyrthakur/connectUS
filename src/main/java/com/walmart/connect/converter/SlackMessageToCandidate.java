package com.walmart.connect.converter;

import com.walmart.connect.constants.Constants;
import com.walmart.connect.model.*;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

@Component
public class SlackMessageToCandidate {

public static Candidate convert(String inputs){

    Candidate candidate=new Candidate();

/*
           Name: vicky thakur
             EmailId:  vickythakur6430@gmail.com
             Skills:  java,spring-boot, SQL, mongo
             Role:    IN3
             Time_Slot:  21-02-2021:14:00 to 20-02-2021:16:00, 22-02-2021:14:00 to 20-02-2021:16:00
             Team: GBS_FINTECH
             Location : BANGALORE
             Round:  TECH_1
             Experience:  2.6
 */


String dataLines[]=inputs.split("\\r?\\n");

    System.out.println(dataLines);

    for(String inp:dataLines){

        String dataSet[];
       if(inp.contains(Constants.TIME_SLOT)){
           dataSet=inp.split(Constants.TIME_SLOT+":");

       }else{
           dataSet=inp.split(":");
       }

        String key;
        if(inp.contains(Constants.TIME_SLOT)){
            key=Constants.TIME_SLOT;

        }else{
            key=dataSet[0].trim();
        }

        String values =dataSet[1].trim();

        if(Constants.NAME.equalsIgnoreCase(key)){
            candidate.setName(values);

        }else if(Constants.EMAIL_ID.equalsIgnoreCase(key)){
            candidate.setEmail(values);

        }
        else if(Constants.SKILLS.equalsIgnoreCase(key)){

            String skills[]=values.split(",");
            candidate.setSkills(Arrays.asList(values));

        }
        else if(Constants.ROLE.equalsIgnoreCase(key)){
            candidate.setRole(Role.valueOf(values));

        }
        else if(Constants.TEAM.equalsIgnoreCase(key)){
            candidate.setDepartment(Department.valueOf(values));

        }
        else if(Constants.TIME_SLOT.equalsIgnoreCase(key)){

            ArrayList availbaleTime= new ArrayList();
            String timeSots[]=values.split(",");


            for(String time:timeSots){

                String timePair[]=time.split("to");
                try {
                    TimePair pairSlots = new TimePair(new SimpleDateFormat("YYYY-MM-DD HH:mm").parse(timePair[0]),
                             new SimpleDateFormat("YYYY-MM-DD HH:mm").parse(timePair[1]));
                    if(null!=pairSlots){
                        availbaleTime.add(pairSlots);

                    }
                }catch (Exception e){


                }
            }


            candidate.setAvailableTimeSlot(availbaleTime);

        }
        else if(Constants.LOCATION.equalsIgnoreCase(key)){
            candidate.setLocation(Location.valueOf(values));

        }
        else if(Constants.ROUND.equalsIgnoreCase(key)){
            candidate.setRound(Round.valueOf(values));

        }
        else if(Constants.EXPERIENCE.equalsIgnoreCase(key)){
            candidate.setExperience(Double.parseDouble(values));

        }

    }

    return candidate;
}


}

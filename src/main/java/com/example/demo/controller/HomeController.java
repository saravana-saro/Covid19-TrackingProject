package com.example.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.model.LocationStates;
import com.example.demo.services.CoronaVirusDataService;
import com.example.demo.services.CovidDataRepository;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Controller
@Configuration
public class HomeController {

    @Autowired
    CoronaVirusDataService crnService;
    
    @Autowired
    private CovidDataRepository repository;
    
    @Autowired
    public void setCrnService(CoronaVirusDataService crnServices) {
    	this.crnService = crnServices;
    }

    @GetMapping("/")
    @Transactional
    public String home(Model model) {
        List<LocationStates> allStates =  crnService.getAllStates();
        int totalReportedCases = allStates.stream().mapToInt(stat -> stat.getLatestTotalDeaths()).sum();
        int totalNewCases = allStates.stream().mapToInt(stat -> stat.getDiffFromPrevay()).sum();
        
        model.addAttribute("locationStats", allStates);
        model.addAttribute("totalReportedCases", totalReportedCases);
        model.addAttribute("totalNewCases", totalNewCases);
        repository.save(allStates);
        return "home";
    }
    
    @RequestMapping(path="/collectionChartyData",produces = {"application/json"})
    @ResponseBody
    public List<LocationStates> collectChartData(Model model)
    {
    	System.out.println("Here View Chart Date...");
    	List<LocationStates> allstates = crnService.getAllStates();
    	int totalDeathReported = allstates.stream().mapToInt(stat->stat.getLatestTotalDeaths()).sum();
    	model.addAttribute("LocationStates",allstates);
    	model.addAttribute("totalDeathsReported",totalDeathReported);
    	return allstates;
    }
    
    @RequestMapping(path="/collectionChartData/country={name}",produces = {"application/json"})
    @ResponseBody
    public Optional<LocationStates> collectChartDataByCountryID(@PathVariable("id")int countryid, Model model)
    {
    	System.out.println("Here View Chart Date ny Country ID...");
    	Optional <LocationStates> locationStates = repository.findById(countryid);
    	return locationStates;
    }
    
    
    @RequestMapping(path="/collectionChartData/country={name}",produces = {"application/json"})
    @ResponseBody
    public LocationStates collectChartDataByCountName(@PathVariable("name") String countryName,Model model)
    {
    	System.out.println("Here view Chart Data by Country Name...");
    	LocationStates locationStates= repository.findByCountry(countryName);
    	return locationStates;
    }
    
    
    
    @RequestMapping(path="/collectionChartData/top={count}",produces = {"application/json"})
    @ResponseBody
    public List<LocationStates> collectChartDataByCountryTop(@PathVariable("count") int count,Model model)
    {
    	System.out.println("Here view Chart Data by Country Name...");
    	List<LocationStates> locationStates= repository.findBycountryBylatestTotalDeaths(count);
    	return locationStates;
    }
    
    @RequestMapping(value="/viewChart",method=RequestMethod.GET)
    public ModelAndView viewChart() {
    	return new ModelAndView("ViewChart").addObject("myURL",new String("http:..lochalhost:8081/collectChartData"));
    }
    
    @GetMapping("viewChart/{id}")
    public String viewCharytByID(@PathVariable("id")int id,Model m)
    {
    	m.addAttribute("id",id);
    	m.addAttribute("myURL","http://localhost:8081/collectChartData/"+id);
    	return "viewChart";
    }
    
    public String viewChartByCountryName(@RequestParam String name,Model m)
    {
    	m.addAttribute("myURL","http://localhost:8081/collectChartData/country?"+name);
    	return "viewChart";
    }
    
    
    
}





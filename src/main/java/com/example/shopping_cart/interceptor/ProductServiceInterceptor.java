package com.example.shopping_cart.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ProductServiceInterceptor implements HandlerInterceptor {
   @Override
   public boolean preHandle(
           HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

      log.info("RequestURI {}",request.getRequestURI());
      log.info("RequestURL {}",request.getRequestURL());
      log.info("getRemoteAddr {}",request.getRemoteAddr());
      log.info("preHandle Method called");



      return true;
   }
   @Override
   public void postHandle(
      HttpServletRequest request, HttpServletResponse response, Object handler, 
      ModelAndView modelAndView) throws Exception {

      log.info("postHandle Method called");
   }
   
   @Override
   public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
      Object handler, Exception exception) throws Exception {
      log.info("afterCompletion Method called");
   }

//   For use of Stream
   {
      List<String> names=List.of("1","2","3");

      List<Integer> names2=new ArrayList<>();
      for(String name: names){
         names2.add(Integer.parseInt(name));
      }
      log.info("names2 {}", names2);

      List<Integer> names3=names.stream().map(Integer::parseInt).toList();

      List<Integer> names4 = names3.stream().filter(integer -> integer <= 2).toList();

      List<StudEntity> list1 = new ArrayList<>();
      list1.add(new StudEntity(1,"A"));
      list1.add(new StudEntity(2,"B"));

      List<StudDTO> list2= list1.stream()
              .map(studEntity -> new StudDTO(studEntity.id,studEntity.name))
              .toList();
   }
}

class StudEntity {
   int id;
   String name;
   StudEntity(int id,String name){
      this.id = id;
      this.name=name;
   }
}

class StudDTO {
   int id;
   String name;
   StudDTO(int id,String name){
      this.id = id;
      this.name=name;
   }
}
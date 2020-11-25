package com.flotta.repository.switchTable;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.flotta.model.registry.Subscription;
import com.flotta.model.registry.User;
import com.flotta.model.switchTable.UserSub;

public interface UserSubRepository extends CrudRepository<UserSub, Long> {

  UserSub findBySubAndBeginDate(Subscription sub, LocalDate date);

  UserSub findFirstBySubOrderByBeginDateDesc(Subscription sub);

  UserSub findFirstBySubAndBeginDateLessThanOrderByBeginDateDesc(Subscription sub, LocalDate date);

  UserSub findFirstBySubAndBeginDateBeforeOrderByBeginDateDesc(Subscription sub, LocalDate date);

  void deleteAllBySub(Subscription sub);

  List<UserSub> findAllBySubAndBeginDateBetween(Subscription sub, LocalDate begin, LocalDate end);

  List<UserSub> findAllByUserOrderByBeginDateDesc(User user);

}

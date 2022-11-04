package com.vmware.tanzu.demo.accounts;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends PagingAndSortingRepository<Account, String>, CrudRepository<Account, String> {

    List<Account> findTop50ByOrderByBalanceDesc();
}

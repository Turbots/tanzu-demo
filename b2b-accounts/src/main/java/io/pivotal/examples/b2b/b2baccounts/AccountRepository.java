package io.pivotal.examples.b2b.b2baccounts;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends PagingAndSortingRepository<Account, String> {

    List<Account> findTop50ByOrderByBalanceDesc();
}

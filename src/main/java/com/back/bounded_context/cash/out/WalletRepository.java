package com.back.bounded_context.cash.out;

import com.back.bounded_context.cash.domain.CashMember;
import com.back.bounded_context.cash.domain.Wallet;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Integer> {
    Optional<Wallet> findByHolder(CashMember holder);

    Optional<Wallet> findByHolderId(int holderId);
}

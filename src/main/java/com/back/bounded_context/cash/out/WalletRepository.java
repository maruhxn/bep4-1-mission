package com.back.bounded_context.cash.out;

import com.back.bounded_context.cash.domain.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Integer> {
}

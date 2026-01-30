package com.aizistral.nochatrestrictions.core;

import com.google.common.collect.ImmutableSet;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.minecraft.TelemetrySession;
import com.mojang.authlib.minecraft.UserApiService;
import com.mojang.authlib.minecraft.report.AbuseReportLimits;
import com.mojang.authlib.yggdrasil.request.AbuseReportRequest;
import com.mojang.authlib.yggdrasil.response.KeyPairResponse;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executor;

public class WrappedUserApiService implements UserApiService {

    private static final UserProperties FORCED_PROPERTIES;

    static {
        ImmutableSet.Builder<UserFlag> flags = ImmutableSet.builder();

        flags.add(UserFlag.CHAT_ALLOWED);
        flags.add(UserFlag.SERVERS_ALLOWED);
        flags.add(UserFlag.REALMS_ALLOWED);

        FORCED_PROPERTIES = new UserProperties(flags.build(), Map.of());

    }


    private final UserApiService service;

    public WrappedUserApiService(UserApiService service) {
        this.service = service;
    }


    @Override
    public UserProperties fetchProperties() throws AuthenticationException {
        return FORCED_PROPERTIES;
    }

    @Override
    public boolean isBlockedPlayer(UUID playerID) {
        return this.service.isBlockedPlayer(playerID);
    }

    @Override
    public void refreshBlockList() {
        this.service.refreshBlockList();
    }

    @Override
    public TelemetrySession newTelemetrySession(Executor executor) {
        return TelemetrySession.DISABLED;
    }

    @Override
    public KeyPairResponse getKeyPair() {
        return this.service.getKeyPair();
    }

    @Override
    public void reportAbuse(AbuseReportRequest request) {
        this.service.reportAbuse(request);
    }

    @Override
    public boolean canSendReports() {
        return this.service.canSendReports();
    }

    @Override
    public AbuseReportLimits getAbuseReportLimits() {
        return this.service.getAbuseReportLimits();
    }
}

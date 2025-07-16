package com.shophub.AuthService.dtos;

import lombok.Data;

@Data
public class LoginResponseDto {
    private RequestStatus requestStatus;

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }
}

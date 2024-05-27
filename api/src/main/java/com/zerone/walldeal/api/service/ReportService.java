package com.zerone.walldeal.api.service;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.zerone.walldeal.api.entity.Report;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

    public void createReport(Report report) {
        Firestore db = FirestoreClient.getFirestore();
        db.collection("reports").document(report.getReportId()).set(report);
    }

}

package com.zeroone.wallpaperdeal.webservice.service.Impl;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.zeroone.wallpaperdeal.webservice.entity.Report;
import com.zeroone.wallpaperdeal.webservice.service.ReportService;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements ReportService {

    @Override
    public void createReport(Report report) {
        Firestore db = FirestoreClient.getFirestore();
        db.collection("reports").document(report.getReportId()).set(report);
    }

}
